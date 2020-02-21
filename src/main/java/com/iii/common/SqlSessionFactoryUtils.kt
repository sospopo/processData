package com.iii.common

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.InputStream


/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:26 下午
 * @Email zhangchunmiao@kkworld.com
 */
object SqlSessionFactoryUtils {
    private var sqlSessionFactory: SqlSessionFactory

    init {
        val resource = "mybatis-config.xml"
        val inputStream: InputStream
        inputStream = Resources.getResourceAsStream(resource)
        sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
    }

    fun getSqlSessionFactory(): SqlSessionFactory { //加入synchronized关键字加锁，主要是为了防止在多线程中多次实例化SqlSessionFactory对象，从而保证SqlSessionFactory的唯一性。
        return sqlSessionFactory
    }

    //openSqlSession方法的作用则是创建SqlSession对象。
    fun openSqlSession(autoCommit: Boolean = false): SqlSession? {
        return sqlSessionFactory.openSession(autoCommit)
    }
}