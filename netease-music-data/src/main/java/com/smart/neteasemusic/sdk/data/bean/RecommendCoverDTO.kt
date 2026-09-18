package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class RecommendCoverDTO(
    /** 名称 */
    @field:Json(name = "name") val name: String? = null,
    /** 封面 */
    @field:Json(name = "imgUrl") var imgUrl: String? = null
)