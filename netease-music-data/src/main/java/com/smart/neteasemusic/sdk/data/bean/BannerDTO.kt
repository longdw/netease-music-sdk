package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/28 15:54
 */
@JsonClass(generateAdapter = true)
@Keep
data class BannerDTO(
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "resourceId") val resourceId: String?,
    @field:Json(name = "targetUrl") val targetUrl: String?,
    @field:Json(name = "resourceType") val resourceType: String?,
)
