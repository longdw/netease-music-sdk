package com.smart.neteasemusic.sdk.base.common

import androidx.annotation.Keep

/**
 * @author david.long 2026/1/5 09:27
 */
object NeConstants {
    private const val API_BASE_URL = "https://openapi.music.163.com"

    fun getApiBaseUrl() = API_BASE_URL
}

object HttpConst {
    const val HTTP_SCHEME = "http"
    const val HTTPS_SCHEME = "https"
    const val REFRESH_TIME = 50 //回调刷新时间（单位ms）

    const val TIME_OUT = 10_000L
}

object CodeConst {
    /** accessToken过期 */
    const val API_ACCESS_TOKEN_OVERDUE = 1406
    const val API_ACCESS_TOKEN_NO_USE = 1408
}

object NeInitErrorCode {
    /** 初始化播放数据错误 */
    const val ERROR_CODE_INIT_PLAYER: Int = 1003

    /** 认证错误 - 未知 */
    const val ERROR_CODE_OAUTH_UNKNOWN = 1000

    /** 认证错误 - deviceInfo为空 */
    const val ERROR_CODE_OAUTH_DEVICE_INFO_NULL = 1001

    /** 认证错误 - appId，appSecret and priKey为空 */
    const val ERROR_CODE_OAUTH_APP_INFO_NULL = 1002

    /** 初始化用户错误 */
    const val ERROR_CODE_INIT_USER = 2000

    /** 无accesstoken */
    const val ERROR_CODE_NO_ACCESS_TOKEN = 1407

    /** RefreshToken过期 */
    const val ERROR_CODE_REFRESH_TOKEN = 1406
}

object ParamConst {
    const val APP_ID = "appId"
    const val SIGN_TYPE = "signType"
    const val RSA_SHA256 = "RSA_SHA256"
    const val SIGN = "sign"
    const val TIMESTAMP = "timestamp"
    const val BIZ_CONTENT = "bizContent"
    const val ACCESS_TOKEN = "accessToken"
    const val DEVICE = "device"
    const val SDK_VER = "sdkVer"
    const val FLOW_FLAG = "flowFlag"

    const val CHANNEL = "channel"
    const val DEVICE_ID = "deviceId"
    const val DEVICE_TYPE = "deviceType"
    const val APP_VER = "appVer"
    const val OS = "os"
    const val OS_VER = "osVer"
    const val BRAND = "brand"
    const val MODEL = "model"
    const val CLIENT_IP = "clientIp"
    const val USER_ID = "userId"
}

@Keep
enum class SkySoundChannelValue(val value: Int) {
    /**
     * 5.1声道flac
     */
    C51(0),

    /**
     * 2声道flac
     */
    STE(1),

    /**
     * 5.1声道m4a
     */
    AAC(2);

    @Keep
    companion object {
        @JvmStatic
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: C51
    }
}

/**
 * 会员类型
 * 数值跟用户详情 VipOpenDetailDto#type 保持一致
 */
object VipType {
    const val FREE = 0
    const val MUSIC_PACKAGE = 1
    const val BLACK_VIP = 6
    const val IOT_CAR_VIP = 13
    const val S_VIP = 15
}

/**
 * 播放类型
 */
enum class PlayType(val value: Int) {
    /** 歌曲 */
    SONG(1),
    /** 播客 */
    VOICE(2);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: SONG
    }
}