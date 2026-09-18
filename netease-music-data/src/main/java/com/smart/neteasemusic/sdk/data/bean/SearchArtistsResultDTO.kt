package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索艺人结果
 * @author keli.sang 2026/1/23 14:34
 */
@JsonClass(generateAdapter = true)
@Keep
data class SearchArtistsResultDTO(
    @field:Json(name = "recordCount") val recordCount: Long,
    @field:Json(name = "records") val records: List<ArtistDTO>? = listOf()
)
