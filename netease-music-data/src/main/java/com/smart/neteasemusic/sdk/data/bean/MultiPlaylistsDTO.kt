package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/23 14:31
 */
@JsonClass(generateAdapter = true)
@Keep
data class MultiPlaylistsDTO(
    @field:Json(name = "recordCount") val recordCount: Long,
    @field:Json(name = "records") val records: List<PlaylistDTO>
)
