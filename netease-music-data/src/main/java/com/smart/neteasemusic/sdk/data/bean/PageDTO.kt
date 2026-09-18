package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 播客分页
 * @author david.long 2026/1/14 09:23
 */
@Keep
@JsonClass(generateAdapter = true)
data class PageDTO(
    @field:Json(name = "size") val size: Long,
    @field:Json(name = "cursor") val cursor: String,
    @field:Json(name = "more") val more: Boolean
)