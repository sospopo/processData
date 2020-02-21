package com.iii.model

/**

 * @Author zhangchunmiao
 * @Date 2020/2/20-8:15 下午
 * @Email zhangchunmiao@kkworld.com
 */
data class PropertyData(
        var id: Long? = 0,
        var itemId: Long = 0,
        var timestamp: Long = 0,
        var value: String = "",
        var average: Double = 0.0,
        var property: Long = 0
)