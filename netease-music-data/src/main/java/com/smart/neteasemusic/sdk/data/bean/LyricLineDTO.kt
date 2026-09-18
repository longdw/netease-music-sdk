package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.JsonClass

/**
 * 歌词解析后的结果
 * @author david.long 2026/2/27 16:48
 */
@JsonClass(generateAdapter = true)
data class LyricLineDTO(
    //每句歌词对应的时间（毫秒）
    val timeStamp: Int,
    val lyricTxt: String
)