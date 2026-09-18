package com.smart.neteasemusic.sdk.data.util

import com.smart.neteasemusic.sdk.data.bean.LyricLineDTO
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.pow

/**
 * @author david.long 2026/2/27 16:50
 */
object LyricUtil {
    private val timePattern = Pattern.compile("\\[\\d{1,2}:\\d{1,2}([.:]\\d{1,3})?]")

    fun parseLyric(lyricText: String?): List<LyricLineDTO>? {
        return lyricText?.split("\n")?.mapNotNull {
            transformLyric(it)
        }
    }

    /**
     * 将类似于[00:29.01]的时间线 转成 29010的时间轴
     * [00:02.020] [00:02.02]
     *
     * @param strLine 原始的单行歌词
     * @return 毫秒
     */
    private fun getTimeOfLine(strLine: String): Int {
        val times = with(strLine) {
            trim { it <= ' ' }.substring(1, this.length - 1).split(":", ".")
        }
        return when {
            times.size < 2 -> {
                -1
            }
            times.size == 2 -> {
                try {
                    val min = times[0].toInt()
                    val sec = times[1].toInt()
                    if (min < 0 || sec < 0 || sec >= 60) {
                        throw NumberFormatException("数字不合法!")
                    }
                    (min * 60 + sec) * 1000
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    -1
                }
            }
            times.size == 3 -> {
                try {
                    val min = times[0].toInt()
                    val sec = times[1].toInt()
                    val millisecond = times[2]
                    val mm = millisecond.toInt()
                    //[00:13.09]  [00:13:19] Integer.parseInt(millisecond)是 9  和 19
                    val mmLength = millisecond.length
                    if (min < 0 || sec < 0 || sec >= 60 || mm < 0 || mm > 999 || mmLength < 1 || mmLength > 3) {
                        throw NumberFormatException("数字不合法!")
                    }
                    val minSecTotal = (min * 60 + sec) * 1000
                    minSecTotal + (mm * 10.0.pow(3.0 - mmLength)).toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    -1
                }
            }
            else -> {
                -1
            }
        }
    }

    private fun transformLyric(line: String): LyricLineDTO? {
        val m: Matcher = timePattern.matcher(line)
        var time = 0
        var begIndex = 0 //匹配该正则后的那串字符串之后的 第一个char的index “[00:29.01]好啊”beindex = 10

        while (m.find()) {
            time = getTimeOfLine(m.group())
            begIndex = m.end()
        }
        var content: String = line.substring(begIndex, line.length)
        content = content.trim { it <= ' ' }
        if (content.startsWith("[") && content.endsWith("]")) {
            content = ""
        }
        if (content.isBlank()) {
            return null
        }
        return LyricLineDTO(time, content)
    }

    /**
     * 根据时间戳查找对应的歌词行
     * @param timestamp 时间戳（毫秒）
     * @param lyricLineList 歌词行列表
     * @return 返回对应的歌词行索引，如果没有找到则返回-1
     */
    fun findShowLine(timestamp: Long, lyricLineList: List<LyricLineDTO>): Int {
        if (timestamp == 0L || lyricLineList.isEmpty()){
            return -1
        }
        var left = 0
        var right = lyricLineList.size
        while (left <= right) {
            val middle = (left + right) / 2
            val middleTime = lyricLineList[middle].timeStamp
            if (timestamp < middleTime) {
                right = middle - 1
            } else {
                if (middle + 1 >= lyricLineList.size || timestamp <= lyricLineList[middle + 1].timeStamp) {
                    return middle
                }
                left = middle + 1
            }
        }
        return -1
    }
}