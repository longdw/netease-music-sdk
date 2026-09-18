package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/19 10:35
 */
@JsonClass(generateAdapter = true)

data class VocalDetailDTO(
    @field:Json(name = "songId") val songId: String?,
    @field:Json(name = "songId") val origin: AudioDTO?,
    @field:Json(name = "songId") val accompany: AudioDTO?,
    @field:Json(name = "songId") val vocal: AudioDTO?,
    @field:Json(name = "songId") val multiTrack: AudioDTO?,
    )
