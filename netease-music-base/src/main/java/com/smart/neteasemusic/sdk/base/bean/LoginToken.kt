package com.smart.neteasemusic.sdk.base.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/1/9 16:09
 */
@JsonClass(generateAdapter = true)
@Keep
data class LoginToken(
    @field:Json(name = "accessToken") val accessToken: String?
)