package com.smart.neteasemusic.business.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.business.common.ShortcutState
import com.smart.neteasemusic.business.ext.thumbnail
import com.smart.neteasemusic.business.play.ShortcutPlayManager
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.OpenApiSDK
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.PlayState
import com.smart.neteasemusic.sdk.player.common.PlayError
import com.smart.neteasemusic.sdk.player.common.PlayError.ALL_SONG_CAN_NOT_PLAY
import com.smart.neteasemusic.sdk.player.flow.PlayerEvent
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import kotlinx.coroutines.launch

/**
 * @author david.long 2026/2/24 17:20
 */
abstract class BasePlayViewModel : BaseViewModel() {

    val shortcutStateLiveState: LiveData<ShortcutState>
        get() = _shortcutStateLiveState

    private val _shortcutStateLiveState = MutableLiveData<ShortcutState>()

    protected fun startObservingPlayState() {
        viewModelScope.launch {
            launch {
                PlayerFlows.playStateStateFlow.collect { playState ->
                    SDKLogUtil.i("[BasePlayViewModel][playerControlStateStateFlow] playerState=$playState, playSource=${ShortcutPlayManager.playSource}")
                    when {
                        playState == PlayState.PLAYING && checkShouldRefresh() -> {
                            _shortcutStateLiveState.postValue(ShortcutState.PLAYING)
                        }
                        playState == PlayState.PAUSED && checkShouldRefresh() -> {
                            _shortcutStateLiveState.postValue(ShortcutState.LOADING)
                        }

                        else -> {
                            //
                        }
                    }
                }
            }

            launch {
                PlayerFlows.playerEvent.collect {  playerEvent ->
                    if (playerEvent is PlayerEvent.Error) {
                        _shortcutStateLiveState.postValue(ShortcutState.ERROR(e = playerEvent.e))
                    }
                }
            }
        }
    }

    /**
     * 通用的快捷播放逻辑
     * @param position 默认-1表示只过滤能播放的歌曲
     */
    private fun startFetchDataAndPlay(
        playlistId: String,
        playSource: String,
        position: Int = -1,
        playType: PlayType = PlayType.SONG,
        block: suspend (() -> State<List<MusicInfo>>)
    ) {
        SDKLogUtil.e("[BasePlayViewModel][startFetchDataAndPlay] playSourceId=${ShortcutPlayManager.playSourceId}, playlistId=$playlistId")
        if (ShortcutPlayManager.playSourceId == playlistId) {
            if (GlobalPlayManager.isPlaying()) {
                GlobalPlayManager.pause()
            } else if (GlobalPlayManager.isPause()) {
                ShortcutPlayManager.loading(playlistId, playSource)
                GlobalPlayManager.play()
            } else {
                SDKLogUtil.i("[BasePlayViewModel][startFetchDataAndPlay] error: playlistId=$playlistId")
            }
            return
        }

        ShortcutPlayManager.loading(playlistId, playSource)
        _shortcutStateLiveState.postValue(ShortcutState.LOADING)

        viewModelScope.launch {
            val state = block()
            if (ShortcutPlayManager.shouldPlay(playlistId)) {
                when(state) {
                    is SUCCESS -> {
                        //如果position=-1，只过滤能播放的歌曲
                        val playResult = if (position == -1) {
                            val filterMusicList = state.data.filter { it.isCanPlay }
                            if (filterMusicList.isEmpty()) {
                                ShortcutPlayManager.error()
                                _shortcutStateLiveState.postValue(ShortcutState.ERROR(errorMsg = PlayError.codeToMsg(ALL_SONG_CAN_NOT_PLAY)))
                                return@launch
                            }
                            GlobalPlayManager.playMusicList(filterMusicList, position, playType)
                        } else {
                            GlobalPlayManager.playMusicList(state.data, position, playType)
                        }
                        if (playResult == PlayError.NONE) {
                            //播放前置检查完毕，一切准备就绪
                            ShortcutPlayManager.prepared()
                            _shortcutStateLiveState.postValue(ShortcutState.PREPARED)
                        } else {
                            ShortcutPlayManager.error()
                            _shortcutStateLiveState.postValue(ShortcutState.ERROR(errorMsg = PlayError.codeToMsg(playResult)))
                        }
                    }
                    is ERROR -> {
                        ShortcutPlayManager.error()
                        _shortcutStateLiveState.postValue(ShortcutState.ERROR())
                    }
                }
            }
        }
    }

    /**
     * 根据歌单id获取歌单里面的歌曲
     */
    protected fun fetchPlaylistMusicList(playlistId: String) {
        startFetchDataAndPlay(playlistId, PlaySource.PLAY_LIST) {
            OpenApiSDK.getPlaylistApi().fetchPlaylistSong(playlistId).apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    /**
     * 根据专辑id获取专辑里面的歌曲
     */
    protected fun fetchAlbumMusicList(playlistId: String) {
        startFetchDataAndPlay(playlistId, PlaySource.PLAY_LIST) {
            OpenApiSDK.getAlbumApi().fetchAlbumSongList(playlistId).apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    protected fun fetchPodcastMusicList(podcastId: String) {
        startFetchDataAndPlay(podcastId, PlaySource.PODCAST, playType = PlayType.VOICE) {
            val state = OpenApiSDK.getPodcastApi().getVoicesInPodcast(podcastId)
            if (state is SUCCESS) {
                val  musicInfoList = mutableListOf<MusicInfo>()
                state.data.records?.map {
                    it.toMusicInfo()
                }?.let { musicInfoList.addAll(it) }
                State.success(musicInfoList)
            } else {
                State.error()
            }
        }
    }

    /**
     * 获取并播放私人漫游
     */
    protected fun fetchRoamingMusicList() {
        startFetchDataAndPlay(PlaySource.FM, PlaySource.FM) {
            OpenApiSDK.getRecommendApi().fetchRoamingMusicList().apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    /**
     * 获取并播放每日推荐
     */
    protected fun fetchDailyRecommendMusicList() {
        startFetchDataAndPlay(PlaySource.DAILY_RECOMMEND, PlaySource.DAILY_RECOMMEND) {
            OpenApiSDK.getRecommendApi().fetchDailyRecommendSong().apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    /**
     * 获取并播放日推mix
     */
    protected fun fetchDailyMixMusicList(
        resourceType: String,
        resourceId: String,
        params: Map<String, String?>
    ) {
        startFetchDataAndPlay(resourceId, resourceType) {
            OpenApiSDK.getRecommendApi().fetchDailyMixMusicList(resourceType, resourceId, params = params).apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    /**
     * 获取场景音乐并播放
     */
    protected fun fetchSceneMusicList(scene: String) {
        startFetchDataAndPlay(scene, PlaySource.SCENE_MUSIC) {
            OpenApiSDK.getRecommendApi().fetchSceneMusicList(scene).apply {
                if (this is SUCCESS) {
                    this.data.forEach {
                        it.coverUrl = it.coverUrl.thumbnail()
                    }
                }
            }
        }
    }

    protected fun playLocalMusicList(
        playlistId: String,
        musicList: List<MusicInfo>,
        position: Int,
        playSource: String = PlaySource.PLAY_LIST,
        playType: PlayType = PlayType.SONG,
        interceptBlock: (() -> Unit)? = null
    ) {
        SDKLogUtil.i("[BasePlayViewModel][playLocalMusicList] playlistId=$playlistId, playlistId=$playlistId, curMusicId=${GlobalPlayManager.getCurrentMusicInfo()?.id}")
        if (playlistId == GlobalPlayManager.getCurrentMusicInfo()?.id) {
            if (interceptBlock != null) {
                interceptBlock.invoke()
                return
            }
            if (GlobalPlayManager.isPlaying()) {
                GlobalPlayManager.pause()
            } else if (GlobalPlayManager.isPause()) {
                ShortcutPlayManager.loading(playlistId, playSource)
                GlobalPlayManager.play()
            } else {
                SDKLogUtil.i("[BasePlayViewModel][startFetchDataAndPlay] error: playlistId=$playlistId")
            }
            return
        }
        startFetchDataAndPlay(playlistId, playSource, position, playType) {
            State.success(musicList)
        }
    }

    /**
     * 是否需要刷新页面，比如有些歌单或歌曲不属于当前页面，就不需要刷新了
     */
    abstract fun checkShouldRefresh(): Boolean

}