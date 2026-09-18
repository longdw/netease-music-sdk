package com.smart.neteasemusic.sdk.base.util

import androidx.annotation.Keep
import androidx.core.util.lruCache
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Keep
object MD5Util {

    private val cache = lruCache<String, String>(maxSize = 500, create = {
        computeMD5Inner(it)
    })

    @JvmStatic
    fun computeMD5(string: String): String {
        return cache[string]
    }

    private fun computeMD5Inner(string: String): String {
        return try {
            val messageDigest = MessageDigest.getInstance("MD5")
            val digestBytes = messageDigest.digest(string.toByteArray())
            bytesToHexString(digestBytes)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException(e)
        }
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuffer()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }
}