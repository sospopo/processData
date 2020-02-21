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

    private val keyJoiner = Joiner.on("_")
    private val valueJoiner = Joiner.on(", ")
    private val logger = LoggerFactory.getLogger(DataProcess::class.java)

    private val gson = Gson()

    @JvmStatic
    fun main(args: Array<String>) {
        val writer = CsvUtil.getWriter(File("result.csv"), Charsets.UTF_8)
        writer.use {
            writer.write(arrayOf("itemId", "timestamp", "embedding"))
            val topPropertyList = propertyDataMapper.queryTopCount(30)
                    .map { it.property }

            val dataList = propertyDataMapper.queryAllIds()
            val total = dataList.size
            var current = 0
            dataList.chunked(100000) {
                var propertyList = propertyDataMapper.queryByIds(it)

                val resultList = propertyList.map { data ->
                    val sortedRes: String = topPropertyList
                            .map { propertyId ->
                                if (propertyId == data.property) {
                                    data.average.toString()
                                } else {
                                    0.0.toString()
                                }
                            }.reduce { acc: String, data: String ->
                                valueJoiner.join(acc, data)
                            }
                    val timestamp = data.timestamp.toString()
                    val itemId = data.itemId.toString()
                    arrayOf(itemId, timestamp, sortedRes)
                }

                writer.write(resultList)
                current += it.size
                logger.info("total : ${total}, current : ${current}")
            }
        }

    }
}