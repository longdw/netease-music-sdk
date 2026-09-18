package com.smart.neteasemusic.sdk.base

import com.smart.neteasemusic.sdk.base.network.ERROR

/**
 * @author david.long 2026/1/6 15:11
 */
interface SdkInitCallback {
    fun onSuccess()
    fun onError(error: ERROR?)
}