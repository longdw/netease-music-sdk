package com.smart.neteasemusic.sdk.player.listener

/**
 * @author david.long 2026/1/20 10:08
 */
internal interface IPlayer {
    fun init()
    fun start(url: String?, startPositionMs: Long, playNow: Boolean, cacheKey: String?)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun isPlaying(): Boolean
    fun isPause(): Boolean
    fun setPlaySpeed(playSpeed: Float)
    fun seekForward(incrementMs: Long)
    fun seekBack(incrementMs: Long)
    fun setVocalVolume(volume: Float)
    fun setBgmVolume(volume: Float)
    fun getCurrentPositionMs(): Long
    fun setEngineCallback(listener: IEngineCallback)
    fun setEngineProgressCallback(listener: IEngineProgressCallback)
}