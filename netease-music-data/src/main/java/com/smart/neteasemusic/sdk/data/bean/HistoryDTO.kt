package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author ezio.ying 2026/6/11 17:38
 */
@JsonClass(generateAdapter = true)
@Keep
data class HistoryMusicInfoDTO(
    @field:Json(name = "songListVo") val songListVo: List<MusicInfoDTO>?
)

@JsonClass(generateAdapter = true)
@Keep
data class HistoryAlbumListDTO(
    @field:Json(name = "records") val records: List<HistoryAlbumRecordDTO>?
)
@JsonClass(generateAdapter = true)
@Keep
data class HistoryAlbumRecordDTO(
    @field:Json(name = "record") val record: AlbumDTO,
    @field:Json(name = "playTime") val playTime: String?
)


@JsonClass(generateAdapter = true)
@Keep
data class HistoryPlayListDTO(
    @field:Json(name = "records") val records: List<HistoryPlayRecordDTO>?
)
@JsonClass(generateAdapter = true)
@Keep
data class HistoryPlayRecordDTO(
    @field:Json(name = "record") val record: PlaylistDTO,
    @field:Json(name = "playTime") val playTime: String?
)

