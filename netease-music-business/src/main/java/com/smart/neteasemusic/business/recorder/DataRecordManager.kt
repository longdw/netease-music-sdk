package com.smart.neteasemusic.business.recorder

import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.business.play.ShortcutPlayManager
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.PlayState
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import java.util.concurrent.ConcurrentHashMap

/**
 * 播放数据回传，用于最近播放模块展示播放记录
 * @author david.long 2026/4/21 15:01
 */
object DataRecordManager {
    private val playbackRecorder = ConcurrentHashMap<String, PlaybackTime>()

    fun init() {
        SmartScope.launchDefault {
            PlayerFlows.playStateStateFlow.collect { playState ->
                if (playState == PlayState.PLAYING) {
                    SDKLogUtil.i("[DataRecordManager][init]")
                    val currentMusic = GlobalPlayManager.getCurrentMusicInfo()
                    //清除当前id外的所有记录
                    playbackRecorder.entries.removeAll { (key, value) ->
                        if (key != currentMusic?.id) {
                            recordEnd(value.musicInfo, "ui")
                            true
                        } else {
                            false
                        }
                    }
                    //如果本地没有记录，才开始记录
                    if (!playbackRecorder.containsKey(currentMusic?.id)) {
                        recordStart(currentMusic)
                    }
                } else if (playState == PlayState.STOP || playState == PlayState.COMPLETED) {
                    val currentMusic = GlobalPlayManager.getCurrentMusicInfo()
                    if (playbackRecorder.containsKey(currentMusic?.id)) {
                        recordEnd(currentMusic, "playend")
                    }
                }
            }
        }
    }

    private fun createPlaybackTime(musicInfo: MusicInfo): PlaybackTime {
        var sourceId: String? = null
        var sourceType: String?
        var categoryId: String? = null
        when(ShortcutPlayManager.playSource) {
            PlaySource.PLAY_LIST -> {
                sourceType = "list"
                sourceId = ShortcutPlayManager.playSourceId
            }
            PlaySource.ALBUM -> {
                sourceType = "album"
                sourceId = ShortcutPlayManager.playSourceId
            }
            PlaySource.DAILY_RECOMMEND -> {
                sourceType = "dailySongRecommend"
                sourceId = "dailySongRecommend"
            }
            PlaySource.FM -> {
                sourceType = "userfm"
                sourceId = "userfm"
            }
            PlaySource.PODCAST -> {
                sourceType = "djradio"
                sourceId = musicInfo.voiceDTO?.getSourceId()?.toString()
                categoryId = musicInfo.voiceDTO?.djradio?.categoryId?.toString()
            }
            PlaySource.SEARCH_MUSIC -> {
                sourceType = "search"
                sourceId = "search"
            }
            else -> {
                sourceType = "unknown"
            }
        }

        val startLogTime = System.currentTimeMillis()
        return PlaybackTime(musicInfo, startLogTime, sourceType, sourceId, categoryId)
    }

    private fun recordStart(musicInfo: MusicInfo?) {
        if (musicInfo == null) return
        val musicId = musicInfo.id
        val newRecord = createPlaybackTime(musicInfo)
        playbackRecorder[musicId] = newRecord
        //开始记录
        DataRecorder.recordStart(musicInfo, newRecord)
    }

    /**
     * @param end 参考 https://developer.music.163.com/st/developer/document?docId=eb0ddaf2efc649e99dffe0677472466a
     */
    private fun recordEnd(musicInfo: MusicInfo?, end: String = "playend") {
        if (musicInfo == null) return
        val playbackTime = playbackRecorder[musicInfo.id]
        playbackRecorder.remove(musicInfo.id)
        playbackTime?.let {
            //结束记录
            DataRecorder.recordEnd(musicInfo, playbackTime, end)
        }
    }

    data class PlaybackTime(
        val musicInfo: MusicInfo,
        val startLogTime: Long,
        val sourceType: String?,
        val sourceId: String?,
        val categoryId: String?
    ) {
        override fun toString(): String {
            return "(id=${musicInfo.id}, sourceType=$sourceType, sourceId=$sourceId, categoryId=$categoryId)"
        }
    }
}