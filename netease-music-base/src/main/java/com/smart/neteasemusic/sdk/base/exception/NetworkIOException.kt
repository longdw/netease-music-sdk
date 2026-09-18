package com.smart.neteasemusic.sdk.base.exception

import java.io.IOException

/**
 * @author david.long 2026/1/9 15:57
 */
class NetworkIOException : IOException {

    constructor(cause: Throwable?): super(cause)
    constructor(msg: String): super(msg)

    override fun printStackTrace() {
        cause?.printStackTrace() ?: super.printStackTrace()
    }
}