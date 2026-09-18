package com.smart.neteasemusic.sdk.player.listener

/**
 * @author david.long 2026/1/26 14:12
 */
internal interface IEngineProgressCallback {
    fun onBufferPercentChanged(bufferPercent: Int)
    fun onProgressChanged(currentPlayTime: Long, totalTime: Long)
}