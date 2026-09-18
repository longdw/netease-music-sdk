package com.smart.neteasemusic.sdk.data.common

import androidx.annotation.Keep

@Keep
object QrCodeResult {
    const val QRCODE_EXPIRE = 800
    const val QRCODE_WAITING = 801
    const val QRCODE_AUTHING = 802
    const val QRCODE_SUCCESS = 803
    const val QRCODE_FAIL = 804
    const val QRCODE_USED = 400
}