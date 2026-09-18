package com.smart.neteasemusic.sdk.player.flow

/**
 * @author david.long 2026/3/9 13:47
 */
sealed class PlayerEvent {
    //加上timestamp的目的是flow会过滤重复的状态
    data class Error(val e: Throwable?, val timestamp: Long = System.currentTimeMillis()) : PlayerEvent()

    //音质切换状态
    data class QualitySwitching(val musicId: String, val quality: Int) : PlayerEvent()
    data class QualitySwitchSuccess(val musicId: String, val quality: Int) : PlayerEvent()
    data class QualitySwitchFailed(val musicId: String, val quality: Int, val e: Throwable?) : PlayerEvent()

    //随心唱切换状态
    data class KSongSwitching(val musicId: String) : PlayerEvent()
    data class KSongSwitchSuccess(val musicId: String, val target: Int) : PlayerEvent()
    data class KSongSwitchFailed(val musicId: String, val error: KSongSwitchException?) : PlayerEvent()
}

open class KSongSwitchException(msg: String?) : Throwable(msg)
class KSongSwitchNeedLoginException(msg: String? = null) : KSongSwitchException(msg)
class KSongSwitchOtherException(msg: String? = null) : KSongSwitchException(msg)