package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/2/2 15:42
 */
@JsonClass(generateAdapter = true)
@Keep
data class PlaylistTagDTO(
    // 标签Id
    @field:Json(name = "id") val id: String,
    // 标签名称
    @field:Json(name = "name") val name: String,
    // 标签种类
    @field:Json(name = "category") val category: Int,
)

