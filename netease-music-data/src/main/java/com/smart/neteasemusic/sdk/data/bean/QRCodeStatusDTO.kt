package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.bean.AccessToken
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 轮询二维码状态
 * @author david.long 2026/1/15 16:59
 */
@JsonClass(generateAdapter = true)
@Keep
data class QRCodeStatusDTO(
    // 二维码状态(800:不存在或过期, 801:等待扫码, 802:授权中, 803:授权登录成功, 804:未知错误)
    @field:Json(name = "status") val status: Int,
    // 二维码状态说明
    @field:Json(name = "msg") val msg: String?,
    // token相关数据
    @field:Json(name = "accessToken") val accessToken: AccessToken?
)
