package com.smart.neteasemusic.sdk.player.listener

/**
 * @author david.long 2026/2/10 18:56
 */
internal interface ICacheManager {
    fun init()
    /**
     * 缓存大小
     * 单位：Byte
     */
    fun getCacheSize(): Long
    fun clearCache()
}