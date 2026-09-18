package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 艺人角色信息
 * @property roleId 身份id
 * @property roleName 身份名称
 * @author david.long 2026/1/14 09:19
 */
@JsonClass(generateAdapter = true)
@Keep
data class ExpertIdentityTypeDTO(
    @field:Json(name = "roleId") val roleId: Int = 0,
    @field:Json(name = "roleName") val roleName: String? = ""
)