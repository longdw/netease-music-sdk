package com.smart.dhu.testneteasesdk

import com.smart.neteasemusic.sdk.base.util.logger.SmartLogUtil

/**
 * @author david.long 2026/1/6 15:27
 */
object LogUtil {

    private const val TAG = "NeteaseMusic"
    private var prefix: String? = null

    fun init(prefix: String?) {
        this.prefix = prefix ?: ""
        SmartLogUtil.plantDebugTree()
    }

    @JvmStatic
    fun v(msg: String?) {
        SmartLogUtil.v(TAG, msg = "[$prefix][${Thread.currentThread().name}] $msg")
    }

    @JvmStatic
    fun d(msg: String?) {
        SmartLogUtil.d(TAG, msg = "[$prefix][${Thread.currentThread().name}] $msg")
    }

    @JvmStatic
    fun i(msg: String?) {
        SmartLogUtil.i(TAG, msg = "[$prefix][${Thread.currentThread().name}] $msg")
    }

    @JvmStatic
    fun w(msg: String?) {
        SmartLogUtil.w(TAG, msg = "[$prefix][${Thread.currentThread().name}] $msg")
    }

    @JvmStatic
    fun e(msg: String?, throwable: Throwable? = null) {
        SmartLogUtil.e(TAG, msg = "[$prefix][${Thread.currentThread().name}] $msg", throwable = throwable)
    }
}