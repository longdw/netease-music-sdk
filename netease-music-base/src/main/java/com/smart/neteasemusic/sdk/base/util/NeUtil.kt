package com.smart.neteasemusic.sdk.base.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author david.long 2026/1/12 08:46
 */
object NeUtil {
    /**
     * 格式化时间戳为可读格式，用于日志输出
     */
    fun formatTime(timeMillis: Long): String {
        return try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timeMillis))
        } catch (e: Exception) {
            timeMillis.toString()
        }
    }
}