package com.smart.neteasemusic.sdk.player

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.audio.KSongValue
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.util.KSongUtil
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.constrainValue
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue
import com.smart.neteasemusic.sdk.player.common.PlayError
import com.smart.neteasemusic.sdk.player.common.PlayMode
import com.smart.neteasemusic.sdk.player.flow.KSongSwitchOtherException
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import com.smart.neteasemusic.sdk.player.listener.IEngineCallback
import com.smart.neteasemusic.sdk.player.listener.IEngineProgressCallback
import com.smart.neteasemusic.sdk.player.repository.BasePlaylistManager
import com.smart.neteasemusic.sdk.player.repository.DB
import com.smart.neteasemusic.sdk.player.repository.DefaultPlaylistManager
import com.smart.neteasemusic.sdk.player.repository.PlayableMusicInfoCache
import com.smart.neteasemusic.sdk.player.repository.RandomPlaylistManager
import com.smart.neteasemusic.sdk.player.util.MusicQualityUtil
import com.smart.neteasemusic.sdk.player.util.PlayModeUtil
import com.smart.neteasemusic.sdk.player.util.PlayPermissionCheckHelper
import com.smart.neteasemusic.sdk.player.util.PlayingDataSource
import java.util.Locale
import java.util.Random

/**
 * 主要处理应该播放哪首歌，以及其他跟业务有关的逻辑判断
 * @author david.long 2026/1/19 16:52
 */
internal object
PlayerProxy {

    private val playService by lazy {
        PlayService()
    }

    private var currentPlayIndex = PlayingDataSource.getPlayIndex()
    private var currentPlayMode = PlayModeUtil.getCurrentPlayMode()
    //记录音质切换的参数1：歌曲id，参数2：目标音质，参数3：回退音质
    private var pendingQualitySwitchTarget: Triple<String, Int, Int>? = null
    //记录随心唱切换的参数1：歌曲id，参数2：随心唱目标状态，参数3：回退随心唱状态
    private var pendingKSongSwitchTarget: Triple<String, Int, Int>? = null

    private var currentMusicInfo: MusicInfo? = null

    private val playlistManager by lazy {
        DefaultPlaylistManager()
    }

    private val randomPlaylistManager by lazy {
        RandomPlaylistManager()
    }

    /**
     * 初始化播放列表数据和当前播放的歌曲数据
     */
    fun init() {
        this.playService.setEngineCallback(object : IEngineCallback {
            override fun onPlayStateChanged(playState: PlayState) {
                PlayerFlows.updatePlayState(playState)
                if (playState == PlayState.PLAYING) {
                    pendingQualitySwitchTarget?.let { (musicId, target, _) ->
                        pendingQualitySwitchTarget = null
                        PlayerFlows.reportQualitySwitchSuccess(musicId, target)
                    }

                    pendingKSongSwitchTarget?.let { (musicId, target, _) ->
                        pendingKSongSwitchTarget = null
                        PlayerFlows.reportKSongSwitchSuccess(musicId, target)
                    }
                } else if (playState == PlayState.COMPLETED) {
                    onComplete()
                }
            }

            @UnstableApi
            override fun onPlayError(e: Throwable?) {

                pendingQualitySwitchTarget?.let { (musicId, target, fallback) ->
                    pendingQualitySwitchTarget = null
                    SDKLogUtil.e("[PlayerProxy][onPlayError] 歌曲：${musicId}, 音质：${target}切换失败，回退到：$fallback")
                    if (musicId == GlobalPlayManager.getCurrentMusicInfo()?.id) {
                        //回退到上个音质
                        MusicQualityUtil.setQuality(fallback)
                        PlayerFlows.reportQualitySwitchFailed(musicId, target, e)
                    }
                }

                pendingKSongSwitchTarget?.let { (musicId, _, fallback) ->
                    pendingKSongSwitchTarget = null
                    SDKLogUtil.e("[PlayerProxy][onPlayError] 歌曲：${musicId}, 随心唱切换失败，回退到：$fallback")
                    if (musicId == GlobalPlayManager.getCurrentMusicInfo()?.id) {
                        PlayerFlows.reportKSongSwitchFailed(musicId, KSongSwitchOtherException())
                        if (fallback == KSongValue.ON.value) {
                            openKSong()
                        } else {
                            closeKSong()
                        }
                    }
                }

                val cause = e?.cause
                if (cause is HttpDataSource.InvalidResponseCodeException) {
                    val url = cause.dataSpec.uri.toString()
                    val httpCode = cause.responseCode
                    if (httpCode == 403 || httpCode == 416) {
                        //403 Forbidden 错误，需要清空url缓存
                        PlayableMusicInfoCache.cleanInvalidUrlCache(url)
                    }
                }
                PlayerFlows.reportError(e)
            }

        })

        this.playService.setEngineProgressCallback(object : IEngineProgressCallback {
            override fun onBufferPercentChanged(bufferPercent: Int) {
                PlayerFlows.updateBufferPercent(bufferPercent)
            }

            override fun onProgressChanged(currentPlayTime: Long, totalTime: Long) {
                PlayingDataSource.savePlayProgress(currentPlayTime)
                PlayerFlows.updateProgress(currentPlayTime, totalTime)
            }

        })

        DB.playlistDao().getMusicListByPlaylist(BasePlaylistManager.PLAY_LIST_ID_DEFAULT)?.let {
            updateDefaultMusicList(it)
            updateRandomMusicList(generateRandomPlayList(it))
            //初始化播放队列
            PlayerFlows.updatePlaylist(it)
        }
//        DB.playlistDao().getMusicListByPlaylist(PlaylistManager.PLAY_LIST_ID_RANDOM)?.let {
//            updateRandomMusicList(it)
//        }
        getCurrentMusicInfo()?.let {
            PlayerFlows.switchCurrentMusic(it)
        }
        playService.init()
    }

    fun addToNext(audioInfo: MusicInfo, playNext: Boolean) {
        val currentPlayAudio = getCurrentMusicInfo()
        //如果要插入的歌曲是当前歌曲
        if (currentPlayAudio?.id == audioInfo.id) {
            if (!isPlaying() && playNext) {
                realPlay(audioInfo)
            }
            return
        }
        val currentAudioList = getCurrentMusicList()
        if (currentAudioList.isEmpty()) {
            //如果没有播放列表
            play(listOf(audioInfo))
            return
        }

        //如果当前播放类型是歌曲，但是要插入是播客声音资源，不走插播流程，直接播放
        if (PlayingDataSource.getPlayType() == PlayType.SONG && audioInfo.voiceDTO != null) {
            play(listOf(audioInfo))
            return
        }
        //如果当前播放的是播客，但是要插入的是歌曲，不走插播流程，直接播放
        if (PlayingDataSource.getPlayType() == PlayType.VOICE && audioInfo.voiceDTO == null) {
            play(listOf(audioInfo))
            return
        }
        currentMusicInfo = currentPlayAudio
        randomPlaylistManager.insertIntoPlaylist(audioInfo)
        val index = playlistManager.insertIntoPlaylist(audioInfo)

        //因为列表的变化可能导致当前播放的index的变化，所以要重新修正当前播放的index
        val curFixedPlayingIndex = playlistManager.playlist.indexOfFirst {
            currentPlayAudio?.id == it.id
        }
        updatePlayIndex(curFixedPlayingIndex)
        SDKLogUtil.i("[PlayerProxy][addToNext] 修正后的playIndex=$curFixedPlayingIndex")

        //通知更新播放队列
        PlayerFlows.updatePlaylist(playlistManager.playlist)

        if (index != -1 && playNext) {
            play(index)
        }
    }

    fun appendMusicToPlaylist(musicList: List<MusicInfo>) {
        randomPlaylistManager.insertIntoPlaylist(musicList)
        playlistManager.insertIntoPlaylist(musicList)

        //通知更新播放队列
        PlayerFlows.updatePlaylist(playlistManager.playlist)
    }

    fun play(
        musicList: List<MusicInfo>,
        index: Int = 0,
        playType: PlayType = PlayType.SONG,
    ): Int {
        if (musicList.isEmpty()) {
            return PlayError.PLAY_LIST_IS_EMPTY
        }
        if (PlayPermissionCheckHelper.isAllCannotPlay(musicList)) {
            return PlayError.ALL_SONG_CAN_NOT_PLAY
        }
        if (index != -1) {
            val checkResult = PlayPermissionCheckHelper.check(musicList[index])
            if (checkResult != PlayError.NONE) {
                return checkResult
            }
        }

        val tempMusicList = musicList.toMutableList()
        //播放类型修正，遍历list，如果发现有voiceDTO属性为空和不为空同时存在的情况，就去掉不为空的数据
        if (tempMusicList.any { it.voiceDTO == null } && tempMusicList.any { it.voiceDTO != null }) {
            tempMusicList.removeIf { it.voiceDTO != null }
        }
        var tempPlayType = playType
        //遍历list，如果发现voiceDTO不为空，但是playType=SONG，需要修正为VOICE
        if (tempMusicList.any { it.voiceDTO != null } && playType == PlayType.SONG) {
            tempPlayType = PlayType.VOICE
        }

        currentMusicInfo = getCurrentMusicInfo()

//        PlayingDataSource.savePlayType(tempPlayType.value)
        savePlayType(tempPlayType)
        updateDefaultMusicList(tempMusicList)
        updateRandomMusicList(generateRandomPlayList(tempMusicList))
        return play(index)
    }

    private fun savePlayType(playType: PlayType) {
        val oldPlayType = PlayingDataSource.getPlayType()
        PlayingDataSource.savePlayType(playType.value)
        if (playType != oldPlayType) {
            PlayerFlows.updatePlayMode(null)
        }
        if (SDKContext.sdkConfig?.openDefaultFeature == true) {
            if (playType != oldPlayType) {
                if (playType == PlayType.SONG) {
                    setPlaySpeed(1.0f)
                    setPlayMode(PlayMode.LOOP)
                } else {
                    setPlaySpeed(PlayingDataSource.getPlaySpeed())
                    setPlayMode(PlayMode.ORDER)
                }
            }
        }
    }

    private fun realPlay(musicInfo: MusicInfo): Int {
        val startPositionMs = PlayingDataSource.getPlayProgress()
        SDKLogUtil.i("[PlayerProxy][realPlay] musicInfo==>$musicInfo")
        SDKLogUtil.i("[PlayerProxy][realPlay] startPositionMs=$startPositionMs")
        //调用stop()，否则后面playService.start(xx)会走直接播放逻辑
        stop()
        //启动音频服务（完整流程）
        return playService.start(musicInfo, startPositionMs)
    }

    private fun updatePlayIndex(playIndex: Int) {
        currentPlayIndex = playIndex
        PlayingDataSource.savePlayIndex(playIndex)
    }

    fun getCurrentPlayIndex(): Int {
        return currentPlayIndex
    }

    fun playPos(pos: Int, playType: PlayType): Int {
        return play(getCurrentMusicList(), pos, playType)
    }

    fun play(): Int {
        return playService.start()
    }

    fun next(): Int {
        val musicList = getCurrentMusicList()
        if (musicList.isEmpty()) {
            return PlayError.PLAY_LIST_IS_EMPTY
        }

        currentMusicInfo = getCurrentMusicInfo()

        if (currentPlayMode == PlayMode.RANDOM) {
            return randomPlayNext()
        }
        val nextIndex = getNextIndex()
        return play(nextIndex)
    }

    fun prev(): Int {
        val musicList = getCurrentMusicList()
        if (musicList.isEmpty()) {
            return PlayError.ALL_SONG_CAN_NOT_PLAY
        }
        if (currentPlayMode == PlayMode.RANDOM) {
            return randomPlayPrev()
        }
        currentMusicInfo = getCurrentMusicInfo()
        val preIndex = getPrevIndex()
        return play(preIndex, isNext = false)
    }

    fun stop() {
        playService.stop()
    }

    fun pause() {
        playService.pause()
    }

    /**
     * 重新播放当前歌曲，用于音质切换后重新播放
     */
    fun replayForPlayQuality(targetQuality: Int, fallbackQuality: Int) {
        GlobalPlayManager.getCurrentMusicInfo()?.let { musicInfo ->
            pendingQualitySwitchTarget = Triple(musicInfo.id, targetQuality, fallbackQuality)
            PlayerFlows.reportQualitySwitching(musicInfo.id, targetQuality)
            replay()
        }
    }

    fun replay(fromStart: Boolean = false) {
        currentMusicInfo = getCurrentMusicInfo()
        val currentPlayIndex = getCurrentPlayIndex()
        if (fromStart) {
            PlayingDataSource.savePlayProgress(0)
        }
        play(currentPlayIndex)
    }

    fun seekTo(positionMs: Long): Int {
        val currentMusic = getCurrentMusicInfo() ?: return -1
        val freeTrail = currentMusic.freeTrail
        SDKLogUtil.i("[PlayerProxy][seekTo] positionMs=$positionMs, freeTrial=$freeTrail")
        val positionNew = if (freeTrail != null) {
            val trailStartTime = freeTrail.start * 1000
            val trialEndTime = freeTrail.end * 1000
            positionMs.toInt().constrainValue(trailStartTime, trialEndTime)
        } else {
            positionMs
        }
        return if (playService.isPlaying() || playService.isPause()) {
            SDKLogUtil.i("[PlayerProxy][seekTo] 1")
            playService.seekTo(positionNew.toLong())
        } else {
            SDKLogUtil.i("[PlayerProxy][seekTo] 2")
            PlayingDataSource.savePlayProgress(positionNew.toLong())
            PlayError.NONE
        }
    }

    fun setPlayMode() {
        PlayModeUtil.changePlayMode()
        currentPlayMode = PlayModeUtil.getCurrentPlayMode()
        SDKLogUtil.i("[PlayerProxy][setPlayMode1] currentPlayMode=$currentPlayMode")
        //如果是随机播放模式，生成随机播放列表
        if (currentPlayMode == PlayMode.RANDOM) {
            val curPlayList = getCurrentMusicList()
            if (curPlayList.isNotEmpty()) {
                updateRandomMusicList(generateRandomPlayList(curPlayList))
            }
        }

        //通知前端
        PlayerFlows.updatePlayMode(currentPlayMode)
    }

    fun setPlayMode(playMode: PlayMode){
        PlayModeUtil.changePlayMode(playMode)
        currentPlayMode = playMode
        SDKLogUtil.i("[PlayerProxy][setPlayMode2] currentPlayMode=$currentPlayMode")
        if (playMode == PlayMode.RANDOM) {
            val curPlayList = getCurrentMusicList()
            if (curPlayList.isNotEmpty()) {
                updateRandomMusicList(generateRandomPlayList(curPlayList))
            }
        }

        //通知前端
        PlayerFlows.updatePlayMode(playMode)
    }

    fun getPlayMode(): PlayMode {
        return PlayModeUtil.getCurrentPlayMode()
    }

    fun isPlaying(): Boolean {
        return playService.isPlaying()
    }

    fun isPause(): Boolean {
        return playService.isPause()
    }

    fun setPlaySpeed(playSpeed: Float) {
        playService.setPlaySpeed(playSpeed)

        //通知前端
        PlayerFlows.updatePlaySpeed(playSpeed)
    }

    fun seekForward(incrementMs: Long) {
        playService.seekForward(incrementMs)
    }

    fun seekBack(incrementMs: Long) {
        playService.seekBack(incrementMs)
    }

    fun getCurrentMusicList(): List<MusicInfo> {
        return playlistManager.playlist
    }

    fun getCurrentMusicInfo(): MusicInfo? {
        if (currentPlayIndex == -1) return null
        val currentMusicList = getCurrentMusicList()
        if (currentMusicList.isNotEmpty()) {
            val safeIndex = currentPlayIndex.constrainValue(0, currentMusicList.size - 1)
            return currentMusicList[safeIndex]
        }
        return null
    }

    fun getPlayProgress(): Long {
        return PlayingDataSource.getPlayProgress()
    }

    fun getPlayIndex(): Int {
        return PlayingDataSource.getPlayIndex()
    }

    fun getCacheSize(): Long {
        return playService.getCacheSize()
    }

    fun clearCache() {
        return playService.clearCache()
    }

    private fun getSafeNextIndex(index: Int, size: Int): Int {
        return (index + 1) % size
    }

    private fun getSafePrevIndex(index: Int, size: Int): Int {
        return ((index - 1 + size) % size) % size
    }

    fun sing(music: MusicInfo): Int {
       return realPlay(music)
    }

}