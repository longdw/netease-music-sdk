package com.smart.neteasemusic.sdk.data.login

import com.smart.neteasemusic.sdk.base.login.LoginState
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.common.QrCodeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

/**
 * @author david.long 2026/1/15 16:51
 */
class LoginApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(LoginApi::class.java)
    }

    private val tag = "[LoginApiImpl]"

    /**
     * 获取二维码并执行登录
     * @param onQRCodeCallback - 返回二维码获取情况，成功后会带上url，通过ZXingUtil将url转成bitmap
     * @param onLoginSuccess - 登录成功
     * @param onLoginExpire - 二维码过期
     */
    suspend fun fetchQRCodeAndLogin(
        onQRCodeCallback: ((State<String>) -> Unit)? = null,
        onLoginSuccess: (() -> Unit)? = null,
        onLoginExpire: (() -> Unit)? = null
    ) {
        //获取二维码
        val qrCodeState = startFetch {
            api.fetchLoginQRCode()
        }
        when(qrCodeState) {
            is SUCCESS -> {
                val qrCodeUrl = qrCodeState.data.qrCodeUrl
                val uniKey = qrCodeState.data.uniKey
                if (qrCodeUrl.isNotBlank() && uniKey.isNotBlank()) {
                    SDKLogUtil.i("$tag QR code fetched: url=${qrCodeUrl.take(30)}...")
                    //将二维码结果返回，用于页面展示
                    onQRCodeCallback?.invoke(SUCCESS(qrCodeUrl))
                    //轮询登录结果
                    pollingLoginResult(uniKey, onLoginSuccess, onLoginExpire)
                } else {
                    SDKLogUtil.i("$tag Invalid QR code: empty url or uniKey")
                    onQRCodeCallback?.invoke(ERROR(message = "Invalid QR code: empty url or uniKey"))
                }
            }

            is ERROR -> {
                SDKLogUtil.i("$tag Failed to fetch QR code: code=${qrCodeState.code}, msg=${qrCodeState.message}")
                onQRCodeCallback?.invoke(ERROR(qrCodeState.error, qrCodeState.code, qrCodeState.message))
            }
        }
    }

    private suspend fun pollingLoginResult(
        key: String,
        onLoginSuccess: (() -> Unit)? = null,
        onLoginExpire: (() -> Unit)? = null
    ) {
        withContext(Dispatchers.IO) {
            while (isActive) {
                runCatching {
                    api.fetchQRCodeStatus(key)
                }.onSuccess {
                    val qrCodeStatus = it.data
                    SDKLogUtil.i("$tag polling status=${qrCodeStatus?.status}, msg=${qrCodeStatus?.msg}")
                    if (qrCodeStatus != null) {
                        when(qrCodeStatus.status) {
                            QrCodeResult.QRCODE_SUCCESS -> {
                                val accessToken = qrCodeStatus.accessToken
                                if (accessToken != null) {
                                    UserManager.authenticatedLogin(accessToken)
                                    withContext(Dispatchers.Main) {
                                        onLoginSuccess?.invoke()
                                    }
                                    return@withContext
                                }
                            }

                            QrCodeResult.QRCODE_EXPIRE, QrCodeResult.QRCODE_USED, QrCodeResult.QRCODE_FAIL -> {
                                SDKLogUtil.e("$tag QR code failed: ${qrCodeStatus.status}")
                                withContext(Dispatchers.Main) {
                                    onLoginExpire?.invoke()
                                    cancel()
                                }
                            }

                            else -> {
                                delay(2000)
                            }
                        }
                    } else {
                        //一般这种情况都是已经登录了
                        withContext(Dispatchers.Main) {
                            val loginState = UserManager.getCurrentLoginState()
                            if (loginState is LoginState.Authenticated) {
                                onLoginSuccess?.invoke()
                            } else {
                                SDKLogUtil.e("$tag please reopen the login page and check if you are logged in")
                            }
                        }
                        cancel()
                    }
                }.onFailure {
                    SDKLogUtil.e("$tag Failed to poll QR code status", it)
                    delay(2000)
                }
            }
        }
    }

}