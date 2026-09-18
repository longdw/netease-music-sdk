package com.smart.neteasemusic.sdk.player

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.smart.neteasemusic.sdk.player.exo.ExoCacheManager
import com.smart.neteasemusic.sdk.player.exo.ExoPlayerEngine
import com.smart.neteasemusic.sdk.player.listener.ICacheManager
import com.smart.neteasemusic.sdk.player.listener.IPlayer

/**
 * 如果要换其他播放器和缓存策略，直接改这里即可
 * @author david.long 2026/1/20 10:09
 */
internal object PlayerFactory {

    /**
     * 创建播放器
     */
    fun makePlayer(): IPlayer {
        return ExoPlayerEngine
    }

    /**
     * 创建缓存策略
     */
    @OptIn(UnstableApi::class)
    fun makeCacheManager(): ICacheManager {
        return ExoCacheManager
    }
}