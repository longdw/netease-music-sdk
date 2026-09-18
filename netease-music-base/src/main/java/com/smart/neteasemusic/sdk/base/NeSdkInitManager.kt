package com.smart.neteasemusic.sdk.base

import android.content.Context
import androidx.core.content.edit
import com.smart.neteasemusic.sdk.base.common.NeInitErrorCode
import com.smart.neteasemusic.sdk.base.common.SdkAuthorized
import com.smart.neteasemusic.sdk.base.config.SdkConfig
import com.smart.neteasemusic.sdk.base.exception.InitException
import com.smart.neteasemusic.sdk.base.login.LoginManager
import com.smart.neteasemusic.sdk.base.login.LoginState
import com.smart.neteasemusic.sdk.base.login.TokenManager
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.oauth.OAuthConst
import com.smart.neteasemusic.sdk.base.statistics.SiteStatisticsManager
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * SDK初始化
 * 注意：整个流程是异步的过程
 * @author david.long 2026/1/6 14:02
 */
object NeSdkInitManager {

    @Volatile
    private var _isInitialized = false
    val isInitialized
        get() = _isInitialized

    fun init(context: Context, sdkConfig: SdkConfig, callback: SdkInitCallback? = null) {
        SDKLogUtil.i("[SdkManager][init] isInitialized=$_isInitialized")
        if (_isInitialized) {
            callback?.onSuccess()
            return
        }
        //基础
        SpStorage.init(context)
        SDKContext.attach(context, sdkConfig)

        SiteStatisticsManager.newInstance().init(context, "com.smart.dhu.netease", true)

        if (!SdkAuthorized.isAuthorizedModel(android.os.Build.MODEL)) {
            SDKLogUtil.e("[SdkManager][init] Unauthorized model: ${android.os.Build.MODEL}")
            callback?.onError(State.error(message = "Unauthorized model: ${android.os.Build.MODEL}"))
            return
        }

        if (!SdkAuthorized.isAuthorizedPackage(context.packageName)) {
            SDKLogUtil.e("[SdkManager][init] Unauthorized package: ${context.packageName}")
            callback?.onError(State.error(message = "Unauthorized package: ${context.packageName}"))
            return
        }

        if (!SdkAuthorized.isWithinValidPeriod(context.packageName)) {
            callback?.onError(State.error(message = "Authorization expired: package: ${context.packageName}"))
            return
        }

        //初始化用户登录状态
        // 配置向下传递
        val transResult = OAuthConst.transConfig(sdkConfig)
        if (!transResult.first) {
            val code: Int = when (transResult.second) {
                OAuthConst.ERROR_CODE_DEVICE_INFO_NULL -> NeInitErrorCode.ERROR_CODE_OAUTH_DEVICE_INFO_NULL
                OAuthConst.ERROR_CODE_APP_INFO_NULL -> NeInitErrorCode.ERROR_CODE_OAUTH_APP_INFO_NULL
                else -> NeInitErrorCode.ERROR_CODE_OAUTH_UNKNOWN
            }
            val msg = transResult.third
            callback?.onError(ERROR(code = code, message = msg, error = InitException(msg)))
            return
        }
        anonymousLoginOrRefreshToken(context.packageName, callback)
    }

    /**
     * 根据当前登录情况来执行匿名登录或刷新token
     */
    private fun anonymousLoginOrRefreshToken(packageName: String, callback: SdkInitCallback?) {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            callback?.onError(
                ERROR(
                    code = NeInitErrorCode.ERROR_CODE_INIT_USER, message = e.message, error = e
                )
            )
        }
        SmartScope.launch(Dispatchers.Main + exceptionHandler) {
            val error = anonymousLoginOrRefreshToken()
            if (error == null) {
                _isInitialized = true
                callback?.onSuccess()
                postRecord(packageName)
            } else {
                callback?.onError(error)
            }
        }
    }

    private fun postRecord(packageName: String) {
        val sp = SpStorage.getMainPreference("sdk_recorder")
        val isRecord = sp.getBoolean("is_record", false)
        SDKLogUtil.i("[SdkManager][postRecord] isRecord=$isRecord")
        if (!isRecord) {
            val baseSiteRequest = SiteStatisticsManager.newInstance().baseSiteRequest
            baseSiteRequest.eventType = "5"
            baseSiteRequest.eventName = "sdk_user"
            baseSiteRequest.extraInfo = packageName
            baseSiteRequest.automobileType = android.os.Build.PRODUCT
            baseSiteRequest.automobileInfo = SDKContext.sdkConfig?.deviceInfo?.deviceId
            SiteStatisticsManager.newInstance().sendDataToServer(baseSiteRequest)
            sp.edit {
                putBoolean("is_record", true)
            }
        }
    }

    private suspend fun anonymousLoginOrRefreshToken(): ERROR?  {
        val loginState = LoginManager.getCurrentLoginState()
        return when (loginState) {
            is LoginState.Anonymous -> {
                //匿名登录
                null
            }

            is LoginState.Authenticated -> {
                //后台偷偷刷新token
                TokenManager.refreshTokenAsync()
                null
            }

            LoginState.NotLoggedIn -> {
                //如果未登录，需要执行匿名登录
                LoginManager.anonymousLogin()
            }
        }
    }
}