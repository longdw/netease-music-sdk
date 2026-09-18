package com.smart.neteasemusic.sdk.base.util

import com.smart.neteasemusic.sdk.base.BuildConfig
import com.smart.neteasemusic.sdk.base.util.logger.SmartLogUtil
import com.smart.neteasemusic.sdk.base.SDKContext


/**
 * SDK中使用此类来打印日志，业务端请勿调用此类，业务端调用请使用[com.smart.dhu.netease.util.LogUtil]
 * @author david.long 2026/1/9 16:41
 */
object SDKLogUtil {
    private var tag = "NeteaseMusicSdk"

    init {
        SmartLogUtil.plantDebugTree()
    }

    @JvmStatic
    fun v(msg: String?) {
        if (SDKContext.sdkConfig == null || SDKContext.sdkConfig?.openSdkLog == true) {
            SmartLogUtil.v(tag, msg = "<${BuildConfig.SDK_VERSION}><${Thread.currentThread().name})> $msg")
        }
    }

    @JvmStatic
    fun d(msg: String?) {
        if (SDKContext.sdkConfig == null || SDKContext.sdkConfig?.openSdkLog == true) {
            SmartLogUtil.d(tag, msg = "<${BuildConfig.SDK_VERSION}><${Thread.currentThread().name}> $msg")
        }
    }

    @JvmStatic
    fun i(msg: String?) {
        if (SDKContext.sdkConfig == null || SDKContext.sdkConfig?.openSdkLog == true) {
            SmartLogUtil.i(tag, msg = "<${BuildConfig.SDK_VERSION}><${Thread.currentThread().name}> $msg")
        }
    }

    @JvmStatic
    fun w(msg: String?) {
        if (SDKContext.sdkConfig == null || SDKContext.sdkConfig?.openSdkLog == true) {
            SmartLogUtil.w(tag, msg = "<${BuildConfig.SDK_VERSION}><${Thread.currentThread().name}> $msg")
        }
    }

    @JvmStatic
    fun e(msg: String?, throwable: Throwable? = null) {
        if (SDKContext.sdkConfig == null || SDKContext.sdkConfig?.openSdkLog == true) {
            SmartLogUtil.e(tag, msg = "<${BuildConfig.SDK_VERSION}><${Thread.currentThread().name}> $msg", throwable = throwable)
        }
    }

    private fun getSDKVersion() = BuildConfig.SDK_VERSION
}