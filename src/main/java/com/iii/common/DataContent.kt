package com.iii.common

import com.iii.mapper.EventDataMapper
import com.iii.mapper.PropertyDataMapper
import org.apache.ibatis.session.SqlSession

/**

 * @Author zhangchunmiao
 * @Date 2020/2/21-1:25 下午
 * @Email zhangchunmiao@kkworld.com
 */
object DataContent {
    var propertyDataMapper: PropertyDataMapper
    var eventDataMapper: EventDataMapper
    init {
        val openSqlSession = SqlSessionFactoryUtils.openSqlSession(true)!!
        propertyDataMapper = openSqlSession.getMapper(PropertyDataMapper::class.java)
        eventDataMapper = openSqlSession.getMapper(EventDataMapper::class.java)
    }


}