package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/18 14:03
 */
@JsonClass(generateAdapter = true)
data class HiresDTO(
    @field:Json(name = "hiResVOList") val hiResVOList: List<HiresDetailDTO>?
)
data class HiresDetailDTO(
    @field:Json(name = "category") val category: String?,
    @field:Json(name = "songDetailVoList") val songDetailVoList: List<MusicInfoDetailDTO>?,
    @field:Json(name = "playlistDetailVoList") val playlistDetailVoList: List<PlaylistDetailDTO>?,
    @field:Json(name = "albumDetailVoList") val albumDetailVoList: List<AlbumDetailDTO>?,
)