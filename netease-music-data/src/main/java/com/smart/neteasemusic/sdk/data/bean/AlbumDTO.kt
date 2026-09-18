package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/1/14 09:18
 */
@JsonClass(generateAdapter = true)
@Keep
data class AlbumDTO(
    // 专辑Id
    @field:Json(name = "id") val id: String,
    // 专辑名
    @field:Json(name = "name") val name: String? = "",
    // 专辑语种
    @field:Json(name = "language") val language: String? = "",
    // 封面
    @field:Json(name = "coverImgUrl") val coverImgUrl: String? = "",
    // 发行公司
    @field:Json(name = "company") val company: String? = "",
    // 风格流派
    @field:Json(name = "genre") val genre: String? = "",
    // 歌手信息
    @field:Json(name = "artists") val artists: List<ArtistDTO>? = listOf(),
    // 中文翻译名
    @field:Json(name = "transName") val transName: String? = "",
    // 别名
    @field:Json(name = "aliaName") val aliaName: String? = "",
    // 简要描述
    @field:Json(name = "briefDesc") val briefDesc: String? = "",
    // 详细描述
    @field:Json(name = "description") val description: String? = "",
    // 发行时间
    @field:Json(name = "publishTime") val publishTime: String? = "",
    // 是否收藏
    @field:Json(name = "subed") val subed: Boolean? = false,
)