package com.smart.neteasemusic.sdk.player.flow

/**
 * @author david.long 2026/3/6 14:25
 */
data class ProgressState(
    val bufferPercent: Int = 0,
    val currentPlayTime: Long = 0,
    val totalTime: Long = 0
)
