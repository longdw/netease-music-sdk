package com.smart.neteasemusic.sdk.data.lyric

import android.util.LruCache
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.bean.LyricInfoDTO
import com.smart.neteasemusic.sdk.data.bean.LyricLineDTO
import com.smart.neteasemusic.sdk.data.util.LyricUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author david.long 2026/2/27 13:28
 */
object LyricManager {

    private const val MAX_CACHE_SIZE = 10

    private val api by lazy {
        NeRetrofit.getRetrofit().create<LyricApi>(LyricApi::class.java)
    }
    private val _lyricStateFlow = MutableStateFlow<LyricLoadState?>(null)
    private val cache by lazy {
        LruCache<String, LyricInfoDTO>(MAX_CACHE_SIZE)
    }
    /**
     * 外部监听歌词变化
     */
    val lyricStateFlow = _lyricStateFlow.asStateFlow()

    /**
     * 获取歌词
     *
     * 注：
     *
     * 1、该方法只在业务侧全局一个地方调用即可
     *
     * 2、需要歌词的地方监听 lyricStateFlow
     *
     * Example:
     * <pre><code>
     * lifecycleScope.launch {
     *     lifecycle.launch(Life.State.STARTING) {
     *         lyricStateFlow.collect { state ->;
     *             when(state) {
     *                     is LyricLoadState.PREPARE -> {
     *                     }
     *                     is LyricLoadState.ERROR -> {
     *                     }
     *                     is LyricLoadState.SUCCESS -> {
     *                         val lyricText = state.data.lyric
     *                     }
     *         }
     *     }
     * }
     *
     * </pre></code>
     */
    fun fetchLyric(songId: String) {
        val localCache = cache[songId]
        if (localCache != null) {
            SDKLogUtil.e("songId=$songId, 有缓存, localCache=${localCache.songId}")
            postData(localCache)
            return
        }
        //只有网络加载才需要准备，这里主要不要挪动到上面去，否则StateFlow因为value值发送过快会丢失
        _lyricStateFlow.value = LyricLoadState.PREPARE
        SDKLogUtil.i("songId=$songId, 没有缓存，联网获取")
        SmartScope.launchUI {
            runCatching {
                val result = api.fetchLyric(songId)
                val data = result.data
                if (result.isSuccess() && data != null) {
                    cache.put(songId, data)
                    postData(data)
                } else {
                    _lyricStateFlow.value = LyricLoadState.ERROR(result.exception, result.code)
                }
            }.onFailure { error ->
                _lyricStateFlow.value = LyricLoadState.ERROR(error)
            }
        }
    }

    private fun postData(lyricInfoDto: LyricInfoDTO) {
        val lyricText = lyricInfoDto.lyric
        val lyricLineList = LyricUtil.parseLyric(lyricText)?.map { lyricLine ->
            LyricLineDTO(lyricLine.timeStamp, lyricLine.lyricTxt)
        }
        _lyricStateFlow.value = LyricLoadState.SUCCESS(lyricInfoDto, lyricLineList)
    }
}

sealed class LyricLoadState {
    object PREPARE : LyricLoadState()
    /**
     * @param originData 原始数据
     * @param lyricLineList 转换过的数据
     */
    data class SUCCESS(val originData: LyricInfoDTO, val lyricLineList: List<LyricLineDTO>?) : LyricLoadState()
    data class ERROR(val error: Throwable? = null, val code: Int? = null) : LyricLoadState()
}