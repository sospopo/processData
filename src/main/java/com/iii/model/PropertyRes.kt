package com.iii.model

import java.sql.Timestamp

/**

 * @Author zhangchunmiao
 * @Date 2020/2/21-6:49 下午
 * @Email zhangchunmiao@kkworld.com
 */
data class PropertyRes(
        var itemId: Long = 0,
        var timestamp: Long = 0,
        var result: String
)