package com.smart.neteasemusic.sdk.player.util

import com.smart.neteasemusic.sdk.base.common.NeSPConst
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.player.common.PlayMode

/**
 * 播放模式工具类
 * @author david.long 2026/1/26 17:05
 */
internal object PlayModeUtil {

    /**
     * 歌曲播放模式和播客播放模式分开
     */
    fun changePlayMode() {
        if (PlayingDataSource.getPlayType() == PlayType.SONG) {
            changeMusicPlayMode()
        } else {
            changePodcastPlayMode()
        }
    }

    fun changePlayMode(playMode: PlayMode) {
        if (PlayingDataSource.getPlayType() == PlayType.SONG) {
            SpStorage.putInt(NeSPConst.KEY_PLAY_MODE, playMode.value)
        } else {
            SpStorage.putInt(NeSPConst.KEY_PODCAST_PLAY_MODE, playMode.value)
        }
    }

    private fun changeMusicPlayMode() {
        val mode = when (getCurrentPlayMode()) {
            PlayMode.LOOP -> {
                PlayMode.SINGLE
            }

            PlayMode.SINGLE -> {
                PlayMode.RANDOM
            }

            PlayMode.RANDOM -> {
                PlayMode.LOOP
            }

            else -> {
                PlayMode.LOOP
            }
        }
        SpStorage.putInt(NeSPConst.KEY_PLAY_MODE, mode.value)
    }

    private fun changePodcastPlayMode() {
        val mode = when (getCurrentPlayMode()) {
            PlayMode.ORDER -> {
                PlayMode.LOOP
            }

            PlayMode.LOOP -> {
                PlayMode.SINGLE
            }

            PlayMode.SINGLE -> {
                PlayMode.RANDOM
            }

            else -> {
                PlayMode.ORDER
            }
        }
        SpStorage.putInt(NeSPConst.KEY_PODCAST_PLAY_MODE, mode.value)
    }

    fun getCurrentPlayMode(): PlayMode {
        if (PlayingDataSource.getPlayType() == PlayType.SONG) {
            return PlayMode.fromInt(SpStorage.getInt(NeSPConst.KEY_PLAY_MODE, PlayMode.LOOP.value))
        }
        return PlayMode.fromInt(SpStorage.getInt(NeSPConst.KEY_PODCAST_PLAY_MODE, PlayMode.ORDER.value))
    }
}