package com.smart.neteasemusic.sdk.player.common

import androidx.annotation.IntDef

/**
 * @author david.long 2026/1/29 09:11
 */
object PlayError {
    /** 可以起播 */
    const val NONE = 0
    /** 所有歌曲均无法播放 */
    const val ALL_SONG_CAN_NOT_PLAY = 1
    /** 歌曲无法播放（可能无版权、需要VIP、需要付费） */
    const val SONG_CAN_NOT_PLAY = 2
    /** 歌曲列表是空的 */
    const val PLAY_LIST_IS_EMPTY = 3
    /** 音频焦点请求失败 */
    const val REQUEST_AUDIO_FOCUS_FAILED = 4
    /** 歌曲无版权 */
    const val NO_COPYRIGHT = 5
    /** 只有VIP才能播放 */
    const val ONLY_VIP_CAN_PLAY = 6
    /** 需要付费播放 */
    const val PAY_CAN_PLAY = 6

    //以下是针对音质切换的error
    /** 需要黑胶会员 */
    const val NEED_BLACK_VIP = 100
    /** 需要SVIP */
    const val NEED_S_VIP = 101
    /** 需要登录 */
    const val NEED_LOGIN = 102

    /** 其他错误 */
    const val OTHER_ERROR = 1000

    fun codeToMsg(code: Int): String {
        return when(code) {
            ALL_SONG_CAN_NOT_PLAY -> "没有可播放的歌曲"
            SONG_CAN_NOT_PLAY -> "歌曲无法播放"
            NO_COPYRIGHT -> "歌曲暂无版权"
            else -> "歌曲无法播放"
        }
    }

    @IntDef(
        NONE,
        ALL_SONG_CAN_NOT_PLAY,
        SONG_CAN_NOT_PLAY
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PlayErrorType
}