package com.smart.neteasemusic.sdk.player.listener

import com.smart.neteasemusic.sdk.player.PlayState

/**
 * 播放器内部使用，外部不需要实现
 * @author david.long 2026/1/20 10:18
 */
internal interface IEngineCallback {
    fun onPlayStateChanged(playState: PlayState)
    fun onPlayError(e: Throwable?)
    fun onPlayUrlExpired(playUrl: String, e: Throwable?) {
        // 默认不处理
    }
}