package com.smart.neteasemusic.business.preload

import com.smart.neteasemusic.business.play.ShortcutPlayManager
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.common.PlayMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * @author david.long 2026/4/10 10:46
 */
abstract class BasePreLoadStrategy : PreLoadStrategy {
    companion object {
        const val DEFAULT_LIMIT = 100
        //倒数第3个触发
        const val PRE_LOAD_THRESHOLD = 3
    }

    protected var tempOffset = 0
    protected var tempHasMore = true

    /**
     * 记录当前列表的offset
     */
    protected abstract val keyOffset: String

    /**
     * 记录当前列表的hasMore
     */
    protected abstract val keyHasMore: String

    /**
     * 每页加载多少条数据
     * 子类可重写
     */
    protected open fun getPageLimit() = DEFAULT_LIMIT

    /**
     * 预加载阈值，表示播放到倒数第N个歌曲时开始预加载
     * 子类可重写
     */
    protected open fun getPreLoadThreshold() = PRE_LOAD_THRESHOLD

    /**
     * 获取下一页数据
     */
    protected abstract suspend fun fetchNextPage(offset: Int): State<List<MusicInfo>>?

    private var job: Job? = null

    override fun onStartPlay() {
        SDKLogUtil.i("[${this::class.java.simpleName}][onStartPlay], this=$this, tempOffset=$tempOffset, tempHasMore=$tempHasMore")
        saveOffset(tempOffset, tempHasMore)
    }

    override fun preLoad(currentPlaylistSize: Int, currentIndex: Int) {
        //只有顺序播放才支持预加载
        val playMode = GlobalPlayManager.getPlayMode()
        if (playMode != PlayMode.LOOP && playMode != PlayMode.ORDER) {
            SDKLogUtil.i("[${this::class.java.simpleName}][preLoad] playMode is not support, only support LOOP or ORDER.")
            return
        }
        if (!hasMore()) {
            SDKLogUtil.i("[${this::class.java.simpleName}][preLoad] hasMore=false")
            return
        }

        SDKLogUtil.i("[${this::class.java.simpleName}][preLoad] currentIndex=$currentIndex, currentPlaylist.size=$currentPlaylistSize, getPreLoadThreshold()=${getPreLoadThreshold()}")
        if (currentIndex < currentPlaylistSize - getPreLoadThreshold() - 1) return

        job?.cancel()
        job = SmartScope.launch {
            val offset = getOffset()
            val state = fetchNextPage(offset)
            if (state is SUCCESS) {
                val musicList = state.data
                SDKLogUtil.i("[${this@BasePreLoadStrategy::class.java.simpleName}][preLoad] success, size=${musicList.size}, playSource=${ShortcutPlayManager.playSource}")
                if (musicList.isNotEmpty() && isActive && ShortcutPlayManager.playSource == this@BasePreLoadStrategy.playSource) {
                    GlobalPlayManager.appendMusicToPlaylist(musicList)
                    saveOffset(offset + musicList.size, musicList.size >= getPageLimit())
                }
            } else if (state is ERROR) {
                SDKLogUtil.i("[${this@BasePreLoadStrategy::class.java.simpleName}][preLoad] error, state=$state")
            }
        }
    }

    override fun clearCache() {
        SpStorage.remove(keyOffset)
        SpStorage.remove(keyHasMore)
        tempOffset = 0
        tempHasMore = true
    }

    protected fun saveOffset(offset: Int, hasMore: Boolean) {
        SpStorage.putInt(keyOffset, offset)
        SpStorage.putBoolean(keyHasMore, hasMore)
    }

    protected open fun getOffset() = SpStorage.getInt(keyOffset, 0)

    protected open fun hasMore() = SpStorage.getBoolean(keyHasMore, true)
}