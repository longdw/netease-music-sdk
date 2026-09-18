package com.smart.neteasemusic.sdk.player

import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.common.PlayError
import com.smart.neteasemusic.sdk.player.common.PlayMode
import com.smart.neteasemusic.sdk.player.repository.PlayableMusicInfoCache
import com.smart.neteasemusic.sdk.player.util.PlayingDataSource

/**
 * @author david.long 2026/1/19 16:49
 */
object GlobalPlayManager {

    /** 默认快进步长 15s */
    const val SEEK_FORWARD_INCREMENT = 15_000L
    /** 默认快退步长 15s */
    const val SEEK_BACK_INCREMENT = 15_000L

    /**
     * @return [PlayError]
     */
    fun play(): Int {
        SDKLogUtil.i("[GlobalPlayManager][play]")
        return PlayerProxy.play()
    }

    fun pause() {
        SDKLogUtil.i("[GlobalPlayManager][pause]")
        PlayerProxy.pause()
    }

    /**
     * @return [PlayError]
     */
    fun prev(): Int {
        SDKLogUtil.i("[GlobalPlayManager][prev]")
        return PlayerProxy.prev()
    }

    /**
     * @return [PlayError]
     */
    fun next(): Int {
        SDKLogUtil.i("[GlobalPlayManager][next]")
        return PlayerProxy.next()
    }

    fun replay() {
        SDKLogUtil.i("[GlobalPlayManager][replay]")
        PlayerProxy.replay()
    }

    /**
     * @return [PlayError]
     */
    fun seekTo(positionMs: Long): Int {
        SDKLogUtil.i("[GlobalPlayManager][seekTo]")
        return PlayerProxy.seekTo(positionMs)
    }

    /**
     * 获取当前播放的进度
     * 单位：ms
     */
    fun getPlayProgress(): Long {
        return PlayerProxy.getPlayProgress()
    }

    /**
     * 获取当前播放的位置
     */
    fun getPlayIndex(): Int {
        return PlayerProxy.getPlayIndex()
    }

    /**
     * 获取缓存大小
     */
    fun getCacheSize(): Long {
        return PlayerProxy.getCacheSize()
    }

    fun clearCache() {
        SDKLogUtil.i("[GlobalPlayManager][clearCache]")
        PlayerProxy.clearCache()
    }

    /**
     * 从指定位置开始播放
     */
    fun playPos(pos: Int, playType: PlayType = PlayType.SONG): Int {
        SDKLogUtil.i("[GlobalPlayManager][playPos] pos=$pos")
        return PlayerProxy.playPos(pos, playType)
    }

    /**
     * 播放单曲，播放列表会替换为单曲
     */
    fun playMusic(musicInfo: MusicInfo, playType: PlayType = PlayType.SONG) {
        SDKLogUtil.i("[GlobalPlayManager][playMusic] musicInfo=${musicInfo.name}")
        playMusicList(listOf(musicInfo), playType)
    }

    /**
     * 播放音频列表，index=-1，表示从0开始播放，且不会检查第0个是否能播放
     * @return [PlayError]
     */
    fun playMusicList(
        musicList: List<MusicInfo>,
        playType: PlayType = PlayType.SONG
    ): Int {
        SDKLogUtil.i("[GlobalPlayManager][playMusicList] musicList.size=${musicList.size}, playType=$playType")
        return playMusicList(musicList, -1, playType)
    }

    /**
     * 播放音频列表，从指定的index位置开始播放
     * @return [PlayError]
     */
    fun playMusicList(
        musicList: List<MusicInfo>,
        index: Int,
        playType: PlayType = PlayType.SONG
    ): Int {
        SDKLogUtil.i("[GlobalPlayManager][playMusicList] musicList.size=${musicList.size}, index=$index, playType=$playType")
        return PlayerProxy.play(musicList, index, playType)
    }

    /**
     * 歌曲类默认播放模式为循环播放[PlayMode.LOOP]
     * 播客类默认播放模式为顺序播放[PlayMode.ORDER]
     * 播放模式，不用指定，SDK内部会自动设置并保存
     */
    fun setPlayMode() {
        SDKLogUtil.i("[GlobalPlayManager][setPlayMode]")
        PlayerProxy.setPlayMode()
    }

    fun setPlayMode(playMode: PlayMode){
        SDKLogUtil.i("[GlobalPlayManager][setPlayModeByVoice]")
        PlayerProxy.setPlayMode(playMode)
    }

    fun getPlayMode(): PlayMode {
        return PlayerProxy.getPlayMode()
    }

    /**
     * 播放倍速
     */
    fun setPlaySpeed(playSpeed: Float) {
        SDKLogUtil.i("[GlobalPlayManager][setPlaySpeed] playSpeed=$playSpeed")
        PlayingDataSource.savePlaySpeed(playSpeed)
        PlayerProxy.setPlaySpeed(playSpeed)
    }

    fun getPlaySpeed(): Float {
        return PlayingDataSource.getPlaySpeed()
    }

    /**
     * 当前播放类型
     * @return [PlayType.SONG] or [PlayType.VOICE]
     */
    fun getPlayType(): PlayType {
        return PlayingDataSource.getPlayType()
    }

    /**
     * 快进，默认30s
     */
    fun seekForward(incrementMs: Long = SEEK_FORWARD_INCREMENT) {
        SDKLogUtil.i("[GlobalPlayManager][seekForward] incrementMs=$incrementMs")
        PlayerProxy.seekForward(incrementMs)
    }

    /**
     * 快退，默认15s
     */
    fun seekBack(incrementMs: Long = SEEK_BACK_INCREMENT) {
        SDKLogUtil.i("[GlobalPlayManager][seekBack] incrementMs=$incrementMs")
        PlayerProxy.seekBack(incrementMs)
    }

    /**
     * 添加到当前播放歌曲的下一首
     * @param playNext 是否立即播放
     */
    fun addToNext(musicInfo: MusicInfo, playNext: Boolean = false) {
        SDKLogUtil.i("[GlobalPlayManager][addToNext] musicInfo=${musicInfo.name}, playNext=$playNext")
        PlayerProxy.addToNext(musicInfo, playNext)
    }

    /**
     * 添加到播放列表末尾
     */
    fun appendMusicToPlaylist(musicList: List<MusicInfo>) {
        SDKLogUtil.i("[GlobalPlayManager][appendMusicToPlaylist] musicList.size=${musicList.size}")
        PlayerProxy.appendMusicToPlaylist(musicList)
    }

    /**
     * 获取当前播放队列
     */
    fun getCurrentPlaylist(): List<MusicInfo> {
        return PlayerProxy.getCurrentMusicList()
    }

    /**
     * 获取当前播放的歌曲
     */
    fun getCurrentMusicInfo(): MusicInfo? {
        val audioInfo = PlayerProxy.getCurrentMusicInfo()
        if (audioInfo is MusicInfo) {
            return audioInfo
        }
        return null
    }

    fun isPlaying(): Boolean {
        val isPlaying = PlayerProxy.isPlaying()
        return isPlaying
    }

    fun isPause(): Boolean {
        val isPause = PlayerProxy.isPause()
        return isPause
    }

    /**
     * 是否开启了随心唱
     */
    fun isOpenKSong(): Boolean {
        val isOpenKSong = PlayerProxy.isOpenKSong()
        SDKLogUtil.i("[GlobalPlayManager][isOpenKSong] isOpenKSong=$isOpenKSong")
        return isOpenKSong
    }

    /**
     * 打开随心唱
     *
     * 1、需要登录后才支持随心唱模式
     *
     * 2、随心唱模式与音质功能冲突，只可开启其一
     */
    fun openKSong() {
        SDKLogUtil.i("[GlobalPlayManager][openKSong]")
        PlayerProxy.openKSong()
    }

    /**
     * 关闭随心唱
     */
    fun closeKSong() {
        SDKLogUtil.i("[GlobalPlayManager][closeKSong]")
        PlayerProxy.closeKSong()
    }

    /**
     * 设置人声音量（范围 0~1）
     *
     * 仅在随心唱模式生效
     */
    fun setVocalVolume(volume: Float) {
        SDKLogUtil.i("[GlobalPlayManager][setVocalVolume] volume=$volume")
        PlayerProxy.setVocalVolume(volume)
    }

    fun getVocalVolume(): Float {
        SDKLogUtil.i("[GlobalPlayManager][getVocalVolume]")
        return PlayerProxy.getVocalVolume()
    }

    /**
     * 设置伴奏音量（范围 0~1）
     *
     * 仅在随心唱模式生效
     */
    fun setBgmVolume(volume: Float) {
        SDKLogUtil.i("[GlobalPlayManager][setBgmVolume] volume=$volume")
        PlayerProxy.setBgmVolume(volume)
    }

    fun getBgmVolume(): Float {
        SDKLogUtil.i("[GlobalPlayManager][getBgmVolume]")
        return PlayerProxy.getBgmVolume()
    }

    /**
     * 退出应用后记得调用此方法，建议放全局service中调用
     */
    fun release() {
        SDKLogUtil.i("[GlobalPlayManager][release]")
        SDKContext.release()
        PlayerProxy.stop()
    }

    /**
     * 退出登录后调用此方法
     */
    fun logout() {
        SDKLogUtil.i("[GlobalPlayManager][logout]")
        PlayableMusicInfoCache.clear()
        PlayerProxy.clear()
        UserManager.logout()
    }

}