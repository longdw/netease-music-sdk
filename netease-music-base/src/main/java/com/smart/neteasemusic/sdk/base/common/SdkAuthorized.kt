package com.smart.neteasemusic.sdk.base.common

/**
 * @author david.long 2026/7/1 16:03
 */
internal object SdkAuthorized {
    init {
        System.loadLibrary("sdkauth")
    }

    fun isAuthorizedModel(model: String): Boolean {
        return nativeIsAuthorizedModel(model)
    }

    fun isAuthorizedPackage(packageName: String): Boolean {
        return nativeIsAuthorizedPackage(packageName)
    }

    fun isWithinValidPeriod(packageName: String): Boolean {
        return nativeIsWithinValidPeriod(packageName)
    }

    private external fun nativeIsAuthorizedModel(model: String): Boolean
    private external fun nativeIsAuthorizedPackage(packageName: String): Boolean
    private external fun nativeIsWithinValidPeriod(packageName: String): Boolean
}