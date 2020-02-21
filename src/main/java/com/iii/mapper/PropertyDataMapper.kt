package com.iii.mapper

import com.iii.common.TableNames
import com.iii.model.PropertyData
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.util.ArrayList

/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:33 下午
 * @Email zhangchunmiao@kkworld.com
 */
interface PropertyDataMapper {

    fun batchInsertData(@Param("dataList") dataList: ArrayList<PropertyData>): Int


    @Select("select t1.property as property, count(*) as count from property t1 group by t1.property order by count desc limit #{top}")
    fun queryTopCount(@Param("top") top: Int): MutableList<PropertyData>

    @Select("select id from ${TableNames.PROPERTY} order by id asc")
    fun queryAllIds(): List<Long>

    fun queryByIds(@Param("ids") ids: List<Long>): MutableList<PropertyData>
}