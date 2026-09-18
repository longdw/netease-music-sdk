package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/6/29 9:04
 */
@JsonClass(generateAdapter = true)
@Keep
data class HiFiZoneHomePageResDTO(
    val positionCode: String? = null,
    val moduleBaseInfos: List<HiFiZoneModuleBaseInfoDTO>? = null,
)

@JsonClass(generateAdapter = true)
@Keep
data class HiFiZoneModulePageResDTO(
    val moduleBaseInfo: HiFiZoneModuleBaseInfoDTO? = null,
)