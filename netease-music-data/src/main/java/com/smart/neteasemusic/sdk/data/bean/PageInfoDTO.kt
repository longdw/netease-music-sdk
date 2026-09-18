package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/23 17:13
 */
@JsonClass(generateAdapter = true)
@Keep
data class PageInfoDTO(
    /** 当前页码 */
    @field:Json(name = "current") val current: Long,
    /** 获取的记录数 */
    @field:Json(name = "size") val size: Long,
    /** 总的记录数 */
    @field:Json(name = "recordCount") val recordCount: Long,
    /** 总页码数 */
    @field:Json(name = "total") val total: Long,
)
