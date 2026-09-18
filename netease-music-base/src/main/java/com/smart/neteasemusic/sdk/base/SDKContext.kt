package com.smart.neteasemusic.sdk.base

import android.content.Context
import com.netease.httpdns.HttpDnsService
import com.smart.neteasemusic.sdk.base.config.SdkConfig
import com.smart.neteasemusic.sdk.base.login.LoginManager

/**
 * @author david.long 2026/2/9 18:46
 */
object SDKContext {

    lateinit var appContext: Context
        private set

    var sdkConfig: SdkConfig? = null
        private set

    fun attach(context: Context, sdkConfig: SdkConfig) {
        LoginManager.init()
        // 使用 Application Context 避免内存泄漏
        this.appContext = context.applicationContext
        this.appContext = context
        this.sdkConfig = sdkConfig
    }

    fun release() {
        HttpDnsService.getInstance().uninit()
    }
}