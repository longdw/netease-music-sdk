package com.smart.neteasemusic.sdk.player.exo

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.listener.ICacheManager
import java.io.File

/**
 * Exo播放器缓存管理
 * @author david.long 2026/2/10 19:00
 */
@UnstableApi
internal object ExoCacheManager : ICacheManager {

    //默认最大的缓存大小500MB
    private const val MAX_CACHE_SIZE = 500L * 1024 * 1024
    private var cache: SimpleCache? = null

    @OptIn(UnstableApi::class)
    override fun init() {
        if (cache == null) {
            val cacheDir = File(SDKContext.appContext.cacheDir, "exo_cache")
            val maxCacheSize = SDKContext.sdkConfig?.maxCacheSize.let {
                if (it == null || it <= 0) {
                    MAX_CACHE_SIZE
                } else {
                    it
                }
            }
            val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
            cache =
                SimpleCache(cacheDir, evictor, StandaloneDatabaseProvider(SDKContext.appContext))
        }
    }

    @OptIn(UnstableApi::class)
    override fun getCacheSize(): Long {
        check()
        var total = 0L
        val keys = cache!!.keys

        keys.forEach { key ->
            SDKLogUtil.i("[ExoCacheManager][getCacheSize] cacheKey=$key")
            val spans = cache!!.getCachedSpans(key)

            for (span in spans) {
                total += span.length
            }
        }
        return total
    }

    private fun check() {
        if (cache == null) throw IllegalArgumentException("please call ExoCacheManager.init() first!")
    }

    @OptIn(UnstableApi::class)
    override fun clearCache() {
        check()
        val cacheKey = ExoPlayerEngine.currentCacheKey ?: GlobalPlayManager.getCurrentMusicInfo()?.playUrl
        val keys = cache!!.keys
        keys.forEach { key ->
            // 检查是否是当前播放的资源
            val isCurrentResource = cacheKey == key
            if (!isCurrentResource) {
                cache!!.removeResource(key)
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun getCache(): SimpleCache {
        check()
        return cache!!
    }
}