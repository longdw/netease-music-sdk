package com.smart.neteasemusic.sdk.base.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 用户相关
 * @author david.long 2026/1/12 08:57
 */
@JsonClass(generateAdapter = true)
@Keep
data class Profile(
    // 用户Id
    @field:Json(name = "id") val id: String?,
    // 用户昵称
    @field:Json(name = "nickname") val nickname: String?,
    // 头像
    @field:Json(name = "avatarUrl") val avatarUrl: String?,
    // 性别, 未知： 0; 男：1; 女：2;
    @field:Json(name = "gender") val gender: String?,
    // 签名
    @field:Json(name = "signature") val signature: String?,
    // 黑胶会员等级
    @field:Json(name = "redVipLevel") val redVipLevel: String?,
    // vip类型
    @field:Json(name = "vipDetail") val vipDetail: List<VipOpenDetailDto>?,
    // 所有vip类型（包含已过期）
    @field:Json(name = "fullVipDetail") val fullVipDetail: List<FullVipOpenDetailDto>?
)

@JsonClass(generateAdapter = true)
@Keep
data class VipOpenDetailDto(
    @field:Json(name = "type") val type: Int,
    @field:Json(name = "expireTime") val expireTime: Long = 0L,
)

@JsonClass(generateAdapter = true)
@Keep
data class FullVipOpenDetailDto(
    @field:Json(name = "type") val type: Int,
    @field:Json(name = "expireTime") val expireTime: Long = 0L,
)