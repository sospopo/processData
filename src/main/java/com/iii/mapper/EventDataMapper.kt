package com.iii.mapper

import com.iii.common.TableNames
import com.iii.model.Event
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:33 下午
 * @Email zhangchunmiao@kkworld.com
 */
interface EventDataMapper {
    fun batchInsertData(@Param("dataList") dataList: List<Event>): Int

    @Select("select count(*) from ${TableNames.EVENT}")
    fun countAll(): Int

    @Select("select id ,item_id ,visitor_id ,timestamp from ${TableNames.EVENT}")
    fun queryAll(): MutableList<Event>
}