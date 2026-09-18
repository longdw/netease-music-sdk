package com.smart.neteasemusic.sdk.base.bean

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.util.MoshiUtils
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass

/**
 * 表示访问令牌及其相关信息的数据类。
 *
 * 此类包含了访问令牌（access token）、刷新令牌（refresh token）以及它们的过期时间信息。
 *
 * @property accessToken 服务器下发的访问令牌。
 * @property refreshToken 用于刷新访问令牌的刷新令牌。
 * @property expireTime 令牌过期时间（以秒为单位）。通常由轮询二维码接口返回。
 * @property expiresTime 令牌过期时间（以秒为单位）。通常由刷新令牌接口返回。
 *
 * Notes：
 * - 匿名令牌不会过期，其生成与设备的 os、brand 和 deviceId 三个参数有关。
 *   例如，对于车辆，如果 vin 码（即 deviceId）不变，该车的匿名令牌就不会改变。
 * - 实名令牌的有效期为 7 天。
 * - [expireTime] 和 [expiresTime] 可能由不同的接口返回，但表示相同的信息。
 */
@JsonClass(generateAdapter = true)
@Keep
data class AccessToken(
    @field:Json(name = "accessToken") val accessToken: String?,
    @field:Json(name = "refreshToken") val refreshToken: String?,
    @field:Json(name = "expireTime") val expireTime: Long?,
    @field:Json(name = "expiresTime") val expiresTime: Long?
) {
    /**
     * 检查当前令牌是否有效。
     *
     * @return 如果 [accessToken] 和 [refreshToken] 都不为空，则返回 true；否则返回 false。
     */
    fun isValid() = accessToken?.isNotEmpty() == true && refreshToken?.isNotEmpty() == true

    companion object {
        val adapter: JsonAdapter<AccessToken> by lazy {
            MoshiUtils.moshiBuild.adapter(AccessToken::class.java)
        }
    }
}