package com.smart.neteasemusic.business.common

import androidx.annotation.StringDef

/**
 * 播放来源
 * @author david.long 2026/2/17 20:27
 */
object PlaySource {
    /** 每日推荐 */
    const val DAILY_RECOMMEND = "1"
    /** 我喜欢 */
    const val MY_LIKE = "2"
    /** 歌单 */
    const val PLAY_LIST = "3"
    /** 专辑 */
    const val ALBUM = "4"
    /** 播客 */
    const val PODCAST = "5"
    /** 私人漫游 */
    const val FM = "6"
    /** 场景音乐 */
    const val SCENE_MUSIC = "7"
    /** banner单曲 */
    const val BANNER_SINGLE_MUSIC = "8"
    /** 搜索歌曲结果 */
    const val SEARCH_MUSIC = "9"
    /** 历史记录 */
    const val HISTORY_MUSIC = "10"
    const val SCENE_FM = "11"

    const val OTHER = "1000"

    @StringDef(
        DAILY_RECOMMEND,
        MY_LIKE,
        PLAY_LIST,
        ALBUM,
        PODCAST,
        FM,
        SCENE_MUSIC,
        BANNER_SINGLE_MUSIC,
        SEARCH_MUSIC,
        HISTORY_MUSIC,
        OTHER
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PlaySourceType
}