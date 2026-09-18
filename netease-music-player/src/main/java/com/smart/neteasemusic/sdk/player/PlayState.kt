package com.smart.neteasemusic.sdk.player

/**
 * @author david.long 2026/1/20 10:19
 */
enum class PlayState {
    IDLE,
    BUFFERING,
    PREPARED,
    PLAYING,
    PAUSED,
    STOP,
    COMPLETED
}