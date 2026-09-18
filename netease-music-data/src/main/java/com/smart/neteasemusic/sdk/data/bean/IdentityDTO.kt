package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 艺人身份
 * @property userType 身份type
 * @property showName 身份名
 * @author david.long 2026/1/14 09:19
 */
@JsonClass(generateAdapter = true)
@Keep
data class IdentityDTO(
    @field:Json(name = "userType") val userType: Int = 0,
    @field:Json(name = "showName") val showName: String? = ""
)