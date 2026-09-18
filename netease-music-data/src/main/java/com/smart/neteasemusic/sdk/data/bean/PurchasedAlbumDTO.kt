package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/6/17 16:04
 */
@JsonClass(generateAdapter = true)
@Keep
data class PurchasedAlbumDTO (
    @field:Json(name = "recordCount")val recordCount: Int,
    @field:Json(name = "records")val records: List<AlbumDTO>
)