package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 登录二维码
 * @author david.long 2026/1/15 16:56
 */
@JsonClass(generateAdapter = true)
@Keep
data class QRCodeDTO(
    // 登录二维码url
    @field:Json(name = "qrCodeUrl") val qrCodeUrl: String,
    // 二维码key
    @field:Json(name = "uniKey") val uniKey: String
)
