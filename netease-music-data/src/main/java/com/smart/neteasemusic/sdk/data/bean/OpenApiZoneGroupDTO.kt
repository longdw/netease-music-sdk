package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/28 16:27
 */
@JsonClass(generateAdapter = true)
@Keep
data class OpenApiZoneGroupDTO(
    val moduleBaseInfos: List<OpenApiZoneGroupItemDTO>,
)

@JsonClass(generateAdapter = true)
@Keep
data class OpenApiZoneGroupItemDTO(
    val style: String? = null,
    val positionCode: String? = null,
    val title: String? = null,
    val resources: List<OpenApiZoneResourceDTO>,
)

/**
 * Suno Ai单个资源合集
 * @author david.long 2024/6/12 16:40
 */
@JsonClass(generateAdapter = true)
@Keep
data class OpenApiZoneResourceDTO(
    val resourceType: String? = null,
    val res: OpenApiZoneResourceItemDTO,
)

@JsonClass(generateAdapter = true)
@Keep
data class OpenApiZoneResourceItemDTO(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String? = "",
    @field:Json(name = "coverImgUrl") val coverImgUrl: String?,
    @field:Json(name = "describe") val describe: String?,
    @field:Json(name = "creatorNickName") val creatorNickName: String?,
    @field:Json(name = "playCount") val playCount: String?,
    @field:Json(name = "subscribedCount") val subscribedCount: Int = 0,
    @field:Json(name = "tags") val tags: String?,
    @field:Json(name = "createTime") val createTime: String?,
    @field:Json(name = "subed") val subed: Boolean? = false,
    @field:Json(name = "trackCount") val trackCount: Int = 0,
    @field:Json(name = "specialType") val specialType: Int = 0,
    @field:Json(name = "allFreeTrialFlag") val allFreeTrialFlag: Boolean? = false,
    @field:Json(name = "creatorId") val creatorId: String?,
)

