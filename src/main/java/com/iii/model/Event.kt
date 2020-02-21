package com.iii.model

/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:35 下午
 * @Email zhangchunmiao@kkworld.com
 */
data class Event(
        var id: Long? = 0,
        var itemId: Long = 0,
        var timestamp: Long = 0,
        var visitorId: Long = 0,
        var result: String = ""
)