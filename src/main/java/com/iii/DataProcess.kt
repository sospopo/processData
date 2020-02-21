package com.iii

import cn.hutool.core.text.csv.CsvUtil
import com.google.common.base.Joiner
import com.google.gson.Gson
import com.iii.common.DataContent
import com.iii.mapper.EventDataMapper
import com.iii.mapper.PropertyDataMapper
import org.slf4j.LoggerFactory
import java.io.File

/**

 * @Author zhangchunmiao
 * @Date 2020/2/21-6:19 下午
 * @Email zhangchunmiao@kkworld.com
 */
object DataProcess {
    private val propertyDataMapper: PropertyDataMapper = DataContent.propertyDataMapper
    private val eventDataMapper: EventDataMapper = DataContent.eventDataMapper

    private val valueJoiner = Joiner.on(", ")
    private val logger = LoggerFactory.getLogger(DataProcess::class.java)

    private val joiner = Joiner.on("_")

    @JvmStatic
    fun main(args: Array<String>) {
        processProperty()
    }


    private fun processProperty() {
        val writer = CsvUtil.getWriter(File("property_result.csv"), Charsets.UTF_8)
        writer.use {
            writer.write(arrayOf("itemId", "timestamp", "embedding"))
            val topPropertyList = propertyDataMapper.queryTopCount(30)
                    .map { it.property }.toSet()

            val dataList = propertyDataMapper.queryAllItemIds()
            val total = dataList.size
            var current = 0
            dataList.chunked(100000) {
                var propertyList = propertyDataMapper.queryByItemIds(it)

                val resultList = propertyList.groupBy { joiner.join(it.timestamp,it.itemId)  }
                        .mapValues { values ->
                            val filterMap = values.value.filter { topPropertyList.contains(it.property) }.map { it.property to it }.toMap()
                            val timestamp = values.value.first().timestamp.toString()
                            val itemId = values.value.first().itemId.toString()

                            val resultValue = topPropertyList.map {
                                if (filterMap.containsKey(it)) {
                                    filterMap[it]!!.average.toString()
                                } else {
                                    0.0.toString()
                                }
                            }.reduce { acc: String, data: String ->
                                valueJoiner.join(acc, data)
                            }

                            arrayOf(itemId, timestamp, resultValue)
                        }.values

                writer.write(resultList)
                current += it.size
                logger.info("total : ${total}, current : ${current}")
            }
        }
    }
}