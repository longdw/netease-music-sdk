package com.smart.neteasemusic.sdk.data.bean

import com.smart.neteasemusic.sdk.base.bean.FreeTrail
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/2/4 15:40
 */
@JsonClass(generateAdapter = true)
data class PodcastUrlDTO(
    val freeTrialInfo: FreeTrail? = null,
    /**
     * 音频大小
     */
    val size: Long = 0,
    /**
     * url类型，当url非空的时候会返回
     * FULL_URL - 完整音频，包含了免费、已付费等
     * TRIAL_URL - 试听片段
     * FEE_GUIDE_URL - 付费引导音频
     */
    val type: String? = "",
    val url: String? = ""
)