package com.smart.neteasemusic.business.preload

import com.smart.neteasemusic.business.NeSdk
import com.smart.neteasemusic.business.play.ShortcutPlayManager
import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.PlayState
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import kotlinx.coroutines.launch

/**
 * 某些播放类型需要预加载
 *
 * 目前仅针对我喜欢歌单类型需要预加载
 *
 * 有额外的播放类型需要预加载，可以调用[addStrategy]添加策略
 *
 * sdk初始化好以后记得调用[init]开启预加载功能
 * @author david.long 2026/4/9 10:25
 */
object PlayerPreLoadManager {

    private var currentMusic: MusicInfo? = null

    fun init() {
        check (NeSdk.isInitialized) {
            "PlayerPreLoadManager#init must be called after netease sdk has been initialized."
        }
        SmartScope.launch {
//            PlayerFlows.switchMusicStateFlow.collect { musicInfo ->
//                if (musicInfo != null && musicInfo != currentMusic) {
//                    currentMusic = musicInfo
//                    onMusicChanged()
//                }
//            }

            PlayerFlows.playStateStateFlow.collect { playState ->
                if (playState == PlayState.PLAYING && currentMusic != GlobalPlayManager.getCurrentMusicInfo()) {
                    currentMusic = GlobalPlayManager.getCurrentMusicInfo()
                    onMusicChanged()
                }
            }
        }
    }

    //注册策略
    private val strategyMap = mutableMapOf<String, PreLoadStrategy>(
        PlaySource.MY_LIKE to MyLikePreLoadStrategy()
        //如果有其他类型需要接入预加载逻辑，参照上面写法，或者调用addStrategy
    )

    /**
     * 外部调用，添加预加载策略
     * @param strategy 预加载策略 参照[MyLikePreLoadStrategy]
     */
    fun addStrategy(strategy: PreLoadStrategy) {
        check (NeSdk.isInitialized)  {
            "netease sdk is not initialized"
        }
        val playSource = strategy.playSource
        require (!strategyMap.containsKey(playSource)) {
            "Strategy already exists"
        }
        strategyMap[playSource] = strategy
    }

    private fun currentStrategy(playSource: String? = null): PreLoadStrategy? {
        return strategyMap[playSource ?: ShortcutPlayManager.playSource]
    }

    /**
     * 业务侧获取歌曲列表时调用，临时记录播放的页数信息，配合[onStartPlay]使用
     * @param playSource 可能无法枚举所有播放类型，超出限制[PlaySource]限制的需要业务侧自行定义
     */
    suspend fun fetchMusicList(playSource: String, params: Map<String, Any> = emptyMap()): State<List<MusicInfo>>? {
        return currentStrategy(playSource)?.fetchMusicList(params)
    }

    /**
     * 开始播放时调用（一般是点击UI上的列表或播放按钮播放）目的是记录页数信息，配合[fetchMusicList]使用
     * @param playSource 可能无法枚举所有播放类型，超出限制[PlaySource]限制的需要业务侧自行定义
     */
    fun onStartPlay(playSource: String) {
        currentStrategy(playSource)?.onStartPlay()
    }

    /**
     * 歌曲切换时触发预加载
     */
    private fun onMusicChanged() {
        SDKLogUtil.i("[PlayerPreLoadManager][onMusicChanged] strategyMap=${strategyMap.keys}")
        val strategy = currentStrategy() ?: run {
            //当前播放类型不支持预加载，清空其他缓存
            strategyMap.values.forEach { it.clearCache() }
            return
        }

        val currentPlaylist = GlobalPlayManager.getCurrentPlaylist()
        val currentMusicInfo = GlobalPlayManager.getCurrentMusicInfo()
        val currentIndex = currentPlaylist.indexOfLast { it.id == currentMusicInfo?.id }

        SDKLogUtil.i("[PlayerPreLoadManager][onMusicChanged] 开始预加载, strategy=$strategy")
        strategy.preLoad(currentPlaylist.size, currentIndex)
    }
}