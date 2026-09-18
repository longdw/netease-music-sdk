package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/3/12 15:34
 *
 * @property voiceId 声音id（可用于获取播放地址）
 * @property programId 声音资源id（可用于获取声音详情）
 * @property encodeVoiceId 加密的声音id
 * @property createTime 创建时间
 * @property coverUrl 封面链接
 * @property name 声音名称
 * @property description 声音描述
 * @property duration 声音时长
 * @property playCount 播放数量
 */
@JsonClass(generateAdapter = true)
@Keep
data class SceneFmVoiceDTO(
    @field:Json(name = "voiceId") val voiceId: String,
    @field:Json(name = "createTime") val createTime: Long,
    @field:Json(name = "coverUrl") val coverUrl: String? = "",
    @field:Json(name = "name") val name: String? = "",
    @field:Json(name = "description") val description: String? = "",
    @field:Json(name = "duration") val duration: Long,
    @field:Json(name = "playCount") val playCount: Long,
    @field:Json(name = "programId") val programId: String,
    @field:Json(name = "resourceType") val resourceType: String?,
    @field:Json(name = "sceneFmPodcastVO") val sceneFmPodcastDTO: SceneFmPodcastDTO?,
)
/**
 *
 * @property name 加密的声音id
 * @property id 创建时间
 * @property subscribeStatus 封面链接
 */
@JsonClass(generateAdapter = true)
@Keep
data class SceneFmPodcastDTO(
    @field:Json(name = "name") val name: String? = "",
    @field:Json(name = "id") val id: String? = "",
    @field:Json(name = "subscribeStatus") val subscribeStatus: Boolean,
)
