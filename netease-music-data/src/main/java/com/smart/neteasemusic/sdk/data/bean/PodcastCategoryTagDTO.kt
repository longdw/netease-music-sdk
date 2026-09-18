package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/3/12 16:57
 */
/**
 * 播客分类标签
 * @property id 标签id
 * @property name 标签name
 */
@JsonClass(generateAdapter = true)
@Keep
data class PodcastCategoryTagDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "coverUrl") val coverUrl: String? = ""
)