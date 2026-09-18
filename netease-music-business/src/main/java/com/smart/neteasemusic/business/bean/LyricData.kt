package com.smart.neteasemusic.business.bean

import android.text.DynamicLayout

/**
 * 歌词控件用到的数据类
 * @author david.long 2026/3/2 13:30
 */
data class LyricData(
    val timeStamp: Int,
    val lyricTxt: String = "",
    var staticLayout: DynamicLayout? = null,
    var lineOffset: Float = Float.MIN_VALUE,
    var highlightProgress: Float = 0f
) {
    fun getHeight(): Int {
        return staticLayout?.height ?: 0
    }
}