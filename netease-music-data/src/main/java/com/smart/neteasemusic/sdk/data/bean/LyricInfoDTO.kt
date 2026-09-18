package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/2/27 13:48
 */
@JsonClass(generateAdapter = true)
data class LyricInfoDTO(
    val songId: String,
    //原版歌词内容（逐行歌词）
    val lyric: String?,
    //歌曲是否本身无歌词，true无歌词（纯音乐）、false有歌词
    val noLyric: Boolean,
    //翻译歌词
    val transLyric: String?,
    //非滚动歌词
    val txtLyric: String?
)
