package com.iii

import cn.hutool.core.text.csv.CsvReadConfig
import cn.hutool.core.text.csv.CsvUtil
import cn.hutool.core.util.NumberUtil
import com.google.common.base.Stopwatch
import com.iii.common.DataContent
import com.iii.common.SqlSessionFactoryUtils
import com.iii.model.Event
import com.iii.model.PropertyData
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.lang.NumberFormatException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList

/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:00 下午
 * @Email zhangchunmiao@kkworld.com
 */
class InitData {
    private val logger = LoggerFactory.getLogger(InitData::class.java)
    private val divide = 1.0E-20
    fun process() {
        val eventsCvsPath = "/Users/zhangchunmiao/Downloads/events.csv"
        val propertiesCvsPath = "/Users/zhangchunmiao/Downloads/properties.csv"


        processEvent(eventsCvsPath)
        processProperty(propertiesCvsPath)
    }

    private fun processProperty(propertiesCvsPath: String) {
        var skip = true
        val file = FileInputStream(propertiesCvsPath)
        var count = AtomicInteger(0)
        var list = arrayListOf<PropertyData>()
        file.use {
            val scanner = Scanner(it)
            var stopWatch = Stopwatch.createStarted()
            while (scanner.hasNextLine()) {
                val line: String = scanner.nextLine()
                if (!skip) {
                    try {
                        val strArray = line.split(",".toRegex()).toTypedArray()
                        val itemId = if (strArray.get(1).isBlank()) null else strArray.get(1).toLong()
                        val timestamp = if (strArray.get(0).isBlank()) null else strArray.get(0).toLong()
                        val property = if (!NumberUtil.isNumber(strArray.get(2))) null else strArray.get(2).toLong()
                        val value = if (strArray.get(3).isBlank()) null else strArray.get(3)

                        if (timestamp != null && itemId != null && property != null && value != null) {
                            val arg = computeArg(value)
                            val propertyData = PropertyData(null, itemId, timestamp, value, arg, property)
                            list.add(propertyData)
                            if (list.size == 10000) {
                                insertDataList(list, count)
                                val costTime = stopWatch.elapsed(TimeUnit.MILLISECONDS)
                                logger.info("there finish $count data,costTime: ${costTime}ms")
                                stopWatch = Stopwatch.createStarted()
                            }
                        }
                    } catch (e: NumberFormatException) {
                        continue
                    }
                } else {
                    skip = false
                }

            }
        }
        if (list.isNotEmpty()) {
            insertDataList(list, count)
        }
    }


    private fun processEvent(eventsCvsPath: String) {

        val csvReadConfig = CsvReadConfig()
        csvReadConfig.setContainsHeader(true)
        val reader = CsvUtil.getReader(csvReadConfig)
        val data = reader.read(File(eventsCvsPath))

        data.chunked(10000) {
            val list = it.mapNotNull { row ->
                val itemId = if (row.get(3).isBlank()) null else row.get(3).toLong()
                val timestamp = if (row.get(0).isBlank()) null else row.get(0).toLong()
                val visitorId = if (row.get(1).isBlank()) null else row.get(1).toLong()
                if (timestamp != null && itemId != null && visitorId != null) {
                    Event(null, itemId, timestamp, visitorId, "")
                } else {
                    null
                }
            }


            val sqlSession = SqlSessionFactoryUtils.openSqlSession()
            if (sqlSession == null) {
                throw NullPointerException()
            }
            sqlSession.use {
                val mapper = DataContent.eventDataMapper
                val batchInsertData = mapper.batchInsertData(list)
            }

        }
    }

    private fun insertDataList(list: ArrayList<PropertyData>, count: AtomicInteger) {
        val mapper = DataContent.propertyDataMapper
        val batchInsertData = mapper.batchInsertData(list)
        val current = count.addAndGet(batchInsertData)


        list.clear()
    }

    fun computeArg(value: String): Double {
        val avg = value.split(" ").toTypedArray().mapNotNull { num ->
            when {
                num.startsWith("n") -> {
                    var s = num.substring(1, num.length)
                    if (s == "Infinity") {
                        s = "1"
                    }
                    s.toDouble()
                }
                num == "Infinity" -> {
                    1.0
                }
                NumberUtil.isNumber(num) -> {
                    num.toDouble()
                }
                else -> {
                    null
                }
            }
        }.average()
        return avg * divide
    }

    object II {
        @JvmStatic
        fun main(args: Array<String>) {
            InitData().process()
        }
    }


}


