package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/6/29 9:05
 */
@JsonClass(generateAdapter = true)
@Keep
data class HiFiZoneModuleBaseInfoDTO(
    val relationId: String? = null,
    val pageCode: String? = null,
    val style: String? = null,
    val positionCode: String? = null,
    val title: String? = null,
    val resources: List<HiFiZoneResourceDetailDTO>? = null
)