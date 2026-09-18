package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/19 10:36
 */
@JsonClass(generateAdapter = true)
data class AudioDTO(
    @field:Json(name = "type") val type: Int?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "duration") val duration: Int?,
    @field:Json(name = "size") val size: Int?,
    @field:Json(name = "md5") val md5: String?,
    @field:Json(name = "audioQuality") val audioQuality: Int?,
    @field:Json(name = "time") val time: Int?,
    @field:Json(name = "level") val level: String?,
    @field:Json(name = "br") val br: Int?,

    )
