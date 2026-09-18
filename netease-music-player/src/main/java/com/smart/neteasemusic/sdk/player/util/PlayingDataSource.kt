package com.smart.neteasemusic.sdk.player.util

import com.smart.neteasemusic.sdk.base.common.NeSPConst
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows

/**
 * 正在播放的数据源管理，包括：
 * 1、播放进度
 * 2、播放列表
 * 3、播放位置
 * 4、播放类型 [PlayType]
 * 5、播放倍速
 * @author david.long 2026/1/27 09:36
 */
internal object PlayingDataSource {

    /**
     * 播放进度
     */
    fun savePlayProgress(currentPlayTime: Long) {
        SpStorage.putLong(NeSPConst.KEY_PLAY_PROGRESS, currentPlayTime)
    }

    fun getPlayProgress(): Long {
        return SpStorage.getLong(NeSPConst.KEY_PLAY_PROGRESS, 0)
    }

    /**
     * 播放位置
     */
    fun savePlayIndex(currentPlayIndex: Int) {
        SpStorage.putInt(NeSPConst.KEY_PLAY_INDEX, currentPlayIndex)
    }

    fun getPlayIndex(): Int {
        return SpStorage.getInt(NeSPConst.KEY_PLAY_INDEX, -1)
    }

    /**
     * 播放类型
     */
    fun savePlayType(playType: Int) {
        SpStorage.putInt(NeSPConst.KEY_PLAY_TYPE, playType)
    }

    fun getPlayType(): PlayType {
        val value = SpStorage.getInt(NeSPConst.KEY_PLAY_TYPE, PlayType.SONG.value)
        return PlayType.fromInt(value)
    }

    /**
     * 播放倍速
     */
    fun savePlaySpeed(playSpeed: Float) {
        SpStorage.putFloat(NeSPConst.KEY_PLAY_SPEED, playSpeed)
    }

    fun getPlaySpeed(): Float {
        return SpStorage.getFloat(NeSPConst.KEY_PLAY_SPEED, 1.0f)
    }

    /**
     * 切歌时这些信息要清空
     */
    fun musicChanged() {
        //清空当前歌曲的音质列表
        PlayerFlows.updateMusicQualityList(null)
        SpStorage.remove(NeSPConst.KEY_PLAY_PROGRESS)
//        SpStorage.remove(NeSPConst.KEY_PLAY_INDEX)
    }

    fun clearPlayProgress() {
        SpStorage.remove(NeSPConst.KEY_PLAY_PROGRESS)
    }

    fun clear() {
        clearPlayProgress()
        SpStorage.remove(NeSPConst.KEY_PLAY_INDEX)
        SpStorage.remove(NeSPConst.KEY_PLAY_SPEED)
    }

    /**
     * 播放的是同一首歌曲，进度也要清空掉
     */
    fun playTheSameMusic() {
        SpStorage.remove(NeSPConst.KEY_PLAY_PROGRESS)
    }

}