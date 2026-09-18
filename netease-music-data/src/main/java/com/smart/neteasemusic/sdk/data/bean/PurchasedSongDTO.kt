package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/6/17 16:04
 */
@JsonClass(generateAdapter = true)
@Keep
data class PurchasedSongDTO (
    @field:Json(name = "recordCount")val recordCount: Int,
    @field:Json(name = "records")val records: List<MusicInfoDTO>,
    @field:Json(name = "emotionTag")val emotionTag: String?,
    @field:Json(name = "vocalFlag")val vocalFlag: String?,
    @field:Json(name = "visible")val visible: Boolean?
)