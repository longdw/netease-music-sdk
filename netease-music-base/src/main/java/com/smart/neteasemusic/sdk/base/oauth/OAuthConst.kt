package com.smart.neteasemusic.sdk.base.oauth

import com.smart.neteasemusic.sdk.base.config.DeviceInfo
import com.smart.neteasemusic.sdk.base.config.SdkConfig

/**
 * 提供全局的常用接口请求参数
 * @author david.long 2026/1/9 10:35
 */
object OAuthConst {
    const val ERROR_CODE_DEVICE_INFO_NULL = "DEVICE_INFO_NULL"
    const val ERROR_CODE_APP_INFO_NULL = "APP_INFO_NULL"

    private const val ERROR_MSG_DEVICE_INFO_NULL = "DeviceInfo could not be null!"
    private const val ERROR_MSG_APP_INFO_NULL =
        "appId，appSecret and priKey could not be null or empty!"

    var sdkConfig: SdkConfig? = null
        private set
    var deviceInfo = DeviceInfo.Builder().build()
        private set
    var priKey: String = ""
        private set
    var appId: String = ""
        private set
    var appSecret: String = ""
        private set

    fun transConfig(sdkConfig: SdkConfig): Triple<Boolean, String, String> {
        this.sdkConfig = sdkConfig
        if (sdkConfig.deviceInfo == null) {
            return Triple(false, ERROR_CODE_DEVICE_INFO_NULL, ERROR_MSG_DEVICE_INFO_NULL)
        }
        this.deviceInfo = sdkConfig.deviceInfo
        priKey = this.sdkConfig?.rsaPriKey ?: ""
        appId = this.sdkConfig?.appId ?: ""
        appSecret = this.sdkConfig?.appSecret ?: ""
        if (appId.isBlank() || appSecret.isBlank() || priKey.isBlank()) {
            return Triple(false, ERROR_CODE_APP_INFO_NULL, ERROR_MSG_APP_INFO_NULL)
        }
        return Triple(true, "", "")
    }
}