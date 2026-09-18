package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/6/17 16:04
 */
@JsonClass(generateAdapter = true)
@Keep
data class PurchasedPodcastDTO (
    @field:Json(name = "records")val records: List<PodcastDTO>,
    @field:Json(name = "page")val page: PageDTO,
    @field:Json(name = "count")val count: Int
)