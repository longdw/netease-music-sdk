package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/19 10:32
 */
@JsonClass(generateAdapter = true)

data class SupportSingDTO(
    @field:Json(name = "songId") val songId: String?,
    @field:Json(name = "hasVocal") val hasVocal: Boolean?,
    @field:Json(name = "hasMultiTrack") val hasMultiTrack: Boolean?,
)
