package com.smart.neteasemusic.sdk.base.common

import androidx.annotation.Keep

@Keep
object NeSPConst {
    /** 播放音质 */
    const val KEY_PLAY_QUALITY = "playQuality"
    /** 播放模式 */
    const val KEY_PLAY_MODE = "playMode"
    /** 播客播放模式（跟歌曲有所不同，多了个顺序播放模式） */
    const val KEY_PODCAST_PLAY_MODE = "podcastPlayMode"
    /** 播放进度 */
    const val KEY_PLAY_PROGRESS = "playProgress"
    /** 当前播放的歌曲在播放列表中的位置 */
    const val KEY_PLAY_INDEX = "playIndex"
    /** 当前播放类型 [PlayType] */
    const val KEY_PLAY_TYPE = "playType"
    /** 播放倍速 */
    const val KEY_PLAY_SPEED = "playSpeed"

    /** K歌开启 */
    const val KEY_OPEN_KSONG = "openKSong"
    /** 人声音量 */
    const val KEY_VOCAL_VOLUME = "vocalVolume"
    /** 伴奏音量 */
    const val KEY_BGM_VOLUME = "bgmVolume"
}