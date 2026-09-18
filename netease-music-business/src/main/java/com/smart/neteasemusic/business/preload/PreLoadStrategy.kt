package com.smart.neteasemusic.business.preload

import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.bean.MusicInfo

/**
 * @author david.long 2026/4/10 10:40
 */
interface PreLoadStrategy {
    /**
     * 播放来源
     * [PlaySource]可能无法枚举所有播放类型，超出限制的需要业务侧自行定义
     */
    val playSource: String

    /**
     * 业务侧加载歌曲列表时调用，用于记录分页状态
     */
    suspend fun fetchMusicList(params: Map<String, Any> = emptyMap()): State<List<MusicInfo>>?

    /**
     * 当开始播放时调用，持久化当前分页状态
     */
    fun onStartPlay()

    /**
     * 触发预加载逻辑，内部判断是否满足条件并请求下一页
     */
    fun preLoad(currentPlaylistSize: Int, currentIndex: Int)

    fun clearCache()
}