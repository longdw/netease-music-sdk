package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索歌曲结果
 * @author keli.sang 2026/1/23 14:35
 */
@JsonClass(generateAdapter = true)
@Keep
data class SearchMusicResultDTO(
    @field:Json(name = "recordCount") val recordCount: Long,
    @field:Json(name = "records") val records: List<MusicInfoDTO>? = listOf(),
)
