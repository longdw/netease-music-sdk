package com.smart.neteasemusic.sdk.player.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 播放器相关的工具类
 * @author david.long 2026/1/26 15:01
 */
object PlayerUtil {

    /**
     * 格式化时间为：HH:mm
     */
    fun formatPlayTime(durationMs: Int): String {
        var time = durationMs
        if (time <= 0) {
            return "00:00"
        }
        time = durationMs / 1000
        val pStr = StringBuilder()
        val min: Int = time / 60
        val sec: Int = time % 60
        if (min < 10) pStr.append(0)
        pStr.append(min)
        pStr.append(":")
        if (sec < 10) pStr.append(0)
        pStr.append(sec)
        return pStr.toString()
    }

    /**
     * 提取url中的时间并转成13位的毫秒时间戳
     */
    fun extractAndConvertTimestamp(url: String?): Long? {
        if (url.isNullOrEmpty()) {
            return null
        }
        // 提取时间字符串
        val pattern = Regex("""/(\d{14})/""")
        val timestampStr = pattern.find(url)?.groupValues?.get(1) ?: return null

        // 转换为毫秒时间戳
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            val localDateTime = LocalDateTime.parse(timestampStr, formatter)
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

}