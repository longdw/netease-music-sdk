package com.smart.neteasemusic.sdk.player.flow

import com.smart.neteasemusic.sdk.base.bean.MusicQuality
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.PlayState
import com.smart.neteasemusic.sdk.player.common.PlayMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author david.long 2026/1/29 16:52
 */
object PlayerFlows {

    /**
     * 包含信息：播放模式、播放速率
     */
    private val _playerControlStateStateFlow = MutableStateFlow(PlayerControlState())
    val playerControlStateStateFlow = _playerControlStateStateFlow.asStateFlow()

    /**
     * 播放状态
     */
    private val _playStateStateFlow = MutableStateFlow(PlayState.IDLE)
    val playStateStateFlow = _playStateStateFlow.asStateFlow()

    /**
     * 切歌时通知
     */
    private val _switchMusicStateFlow = MutableStateFlow<MusicInfo?>(null)
    val switchMusicStateFlow = _switchMusicStateFlow.asStateFlow()

    /**
     * 当歌曲信息更新时通知，常用来做如：收藏状态更新、当前歌曲播放的音质更新、随心唱状态更新
     */
    private val _currentMusicInfoUpdateSharedFlow = MutableSharedFlow<MusicInfo?>(
        extraBufferCapacity = 1,
        replay = 1
    )
    val currentMusicInfoUpdateSharedFlow = _currentMusicInfoUpdateSharedFlow.asSharedFlow()

    /**
     * 当前播放的歌曲对应的音质列表返回时通知
     * first: 歌曲id
     * second: 音质列表
     */
    private val _musicQualityListStateFlow = MutableStateFlow<Pair<String, List<MusicQuality>>?>(null)
    val musicQualityListStateFlow = _musicQualityListStateFlow.asStateFlow()

    /**
     * 播放队列变化时通知
     */
    private val _playlistStateFlow = MutableStateFlow<List<MusicInfo>?>(null)
    val playlistStateFlow = _playlistStateFlow.asStateFlow()

    /**
     * 播放进度变化时通知
     */
    private val _progressStateFlow = MutableStateFlow(ProgressState())
    val progressStateFlow = _progressStateFlow.asStateFlow()

    /**
     * 播放错误时通知
     */
    private val _playerEvent = MutableSharedFlow<PlayerEvent>(extraBufferCapacity = 1)
    val playerEvent = _playerEvent.asSharedFlow()

    /**
     * 当前播放的歌曲（当切歌时后会调用）
     */
    internal fun switchCurrentMusic(musicInfo: MusicInfo?) {
        SDKLogUtil.i("[PlayerFlow][switchCurrentMusic] musicInfo=$musicInfo")
        _progressStateFlow.value = ProgressState()
        _switchMusicStateFlow.value = musicInfo
    }

    /**
     * 更新当前播放歌曲的信息
     */
    internal fun updateCurrentMusicInfo(musicInfo: MusicInfo?) {
        _currentMusicInfoUpdateSharedFlow.tryEmit(musicInfo)
    }

    /**
     * 播放状态
     */
    internal fun updatePlayState(playState: PlayState) {
        _playStateStateFlow.value = playState
    }

    /**
     * 播放模式
     */
    internal fun updatePlayMode(playMode: PlayMode) {
        _playerControlStateStateFlow.update {
            it.copy(playMode = playMode)
        }
    }

    /**
     * 播放速度
     */
    internal fun updatePlaySpeed(playSpeed: Float) {
        _playerControlStateStateFlow.update {
            it.copy(playSpeed = playSpeed)
        }
    }

    /**
     * 更新音质列表
     * Pair的第一个参数表示歌曲id，第二个参数表示音质列表
     */
    internal fun updateMusicQualityList(pair: Pair<String, List<MusicQuality>>?) {
        _musicQualityListStateFlow.value = pair
    }

    /**
     * 更新播放队列
     */
    internal fun updatePlaylist(musicList: List<MusicInfo>) {
        _playlistStateFlow.value = musicList
    }

    /**
     * 更新缓冲比例
     */
    internal fun updateBufferPercent(bufferPercent: Int) {
        _progressStateFlow.update {
            it.copy(bufferPercent = bufferPercent)
        }
    }

    /**
     * 更新播放进度
     */
    internal fun updateProgress(currentPlayTime: Long, totalTime: Long) {
        _progressStateFlow.update {
            it.copy(
                currentPlayTime = currentPlayTime,
                totalTime = totalTime
            )
        }
    }

    /**
     * 更新随心唱状态
     */
    internal fun updateKSongState(musicInfo: MusicInfo) {
        _currentMusicInfoUpdateSharedFlow.tryEmit(musicInfo)
    }

    internal fun reportError(e: Throwable?) {
        val result = _playerEvent.tryEmit(PlayerEvent.Error(e))
        SDKLogUtil.e("reportError(): result=$result")
    }

    //~~~~~~~~~~~~~~~~~~~~音质切换状态通知~~~~~~~~~~~~~~~~~~~~
    internal fun reportQualitySwitching(musicId: String, quality: Int) {
        val result = _playerEvent.tryEmit(PlayerEvent.QualitySwitching(musicId, quality))
        SDKLogUtil.e("[PlayerFlows][reportQualitySwitching] result=$result")
    }

    internal fun reportQualitySwitchSuccess(musicId: String, quality: Int) {
        val result = _playerEvent.tryEmit(PlayerEvent.QualitySwitchSuccess(musicId, quality))
        SDKLogUtil.e("[PlayerFlows][reportQualitySwitchSuccess] result=$result")
    }

    internal fun reportQualitySwitchFailed(musicId: String, quality: Int, e: Throwable?) {
        val result = _playerEvent.tryEmit(PlayerEvent.QualitySwitchFailed(musicId, quality, e))
        SDKLogUtil.e("[PlayerFlows][reportQualitySwitchFailed] result=$result")
    }

    //~~~~~~~~~~~~~~~~~~~~随心唱切换状态通知~~~~~~~~~~~~~~~~~~~~
    internal fun reportKSongSwitching(musicId: String) {
        val result = _playerEvent.tryEmit(PlayerEvent.KSongSwitching(musicId))
        SDKLogUtil.e("[PlayerFlows][reportKSongSwitching] result=$result")
    }

    internal fun reportKSongSwitchSuccess(musicId: String, target: Int) {
        val result = _playerEvent.tryEmit(PlayerEvent.KSongSwitchSuccess(musicId, target))
        SDKLogUtil.e("[PlayerFlows][reportKSongSwitchSuccess] result=$result")
    }

    internal fun reportKSongSwitchFailed(musicId: String, e: KSongSwitchException?) {
        val result = _playerEvent.tryEmit(PlayerEvent.KSongSwitchFailed(musicId, e))
        SDKLogUtil.e("[PlayerFlows][reportKSongSwitchFailed] result=$result")
    }
}