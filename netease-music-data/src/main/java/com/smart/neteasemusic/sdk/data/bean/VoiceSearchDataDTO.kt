package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/6/13 14:03
 */
@JsonClass(generateAdapter = true)
data class VoiceSearchDataDTO(
    @field:Json(name = "albums") val albums: List<AlbumDetailDTO>,
    @field:Json(name = "songs") val songs: List<MusicInfoDTO>,
    @field:Json(name = "voices") val voices: List<VoiceDTO>,
    @field:Json(name = "playlists") val playlists: List<PlaylistDTO>,
    @field:Json(name = "resTypes") val resTypes: List<String>,
    @field:Json(name = "tips") val tips: List<SiriTipsVO>?,
)

data class SiriTipsVO(
    @field:Json(name = "type") val type: Int,
    @field:Json(name = "content") val content: String
)