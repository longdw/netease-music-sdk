package com.smart.neteasemusic.sdk.base.util

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.audio.KSongValue
import com.smart.neteasemusic.sdk.base.common.NeSPConst
import com.smart.neteasemusic.sdk.base.storage.SpStorage

@Keep
object KSongUtil {
    //是否开启 随心唱（K歌）
    private const val ON = 1 //开启
    private const val OFF = 0 //关闭
    private const val NONE = -1
    private var switch = -2
    private var openKSong: Boolean = false
    var lastOpenKSong :Boolean?= null

    @JvmStatic
    fun isOpenKSong(): Boolean {
        if (switch != -1) {
            switch = SpStorage.getInt(NeSPConst.KEY_OPEN_KSONG, NONE)
            when (switch) {
                OFF -> {
                    openKSong = false
                }

                ON -> {
                    openKSong = true
                }
            }
        }
        return openKSong
    }

    /**
     * @param kSongValue 0 关闭 1 开启
     */
    @JvmStatic
    fun saveOpenKSong(kSongValue: KSongValue) {
        switch = kSongValue.value
        when (switch) {
            ON -> {
                openKSong = true
            }

            OFF -> {
                openKSong = false
            }
        }
        SpStorage.putInt(NeSPConst.KEY_OPEN_KSONG, switch)
    }

    @JvmStatic
    //存储人声设置音量
    fun saveVocalVolume(volume: Float) {
        SpStorage.putFloat(NeSPConst.KEY_VOCAL_VOLUME, volume)
    }

    @JvmStatic
    //存储伴奏设置音量
    fun saveBgmVolume(volume: Float) {
        SpStorage.putFloat(NeSPConst.KEY_BGM_VOLUME, volume)
    }

    @JvmStatic
    // 获取人声设置值
    fun getVocalVolume(): Float {
        return SpStorage.getFloat(NeSPConst.KEY_VOCAL_VOLUME, 1f)
    }

    @JvmStatic
    //获取伴奏设置值
    fun getBgmVolume(): Float {
        return SpStorage.getFloat(NeSPConst.KEY_BGM_VOLUME, 1f)
    }
}