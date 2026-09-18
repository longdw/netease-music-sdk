package com.smart.neteasemusic.business.recorder

import com.smart.neteasemusic.business.recorder.DataRecordManager.PlaybackTime
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue
import com.smart.neteasemusic.sdk.player.util.MusicQualityHelper

/**
 * @author david.long 2026/4/21 14:03
 */
object DataRecorder {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(DataRecorderApi::class.java)
    }

    fun recordStart(musicInfo: MusicInfo, playbackTime: PlaybackTime) {
        val id = musicInfo.id
        val alg = musicInfo.alg
        val type = if (musicInfo.voiceDTO != null) {
            "dj"
        } else {
            "song"
        }
        val bitrate = if (musicInfo.voiceDTO != null) {
            MusicQualityBitrateValue.LQ_BITRATE.bitrate
        } else {
            MusicQualityBitrateValue.fromQuality(MusicQualityHelper.getCurrentQuality()).bitrate
        }.div(1000)

        SDKLogUtil.i("[DataRecorder][recordStart] id=$id, playbackTime=${playbackTime}")
        SmartScope.launchDefault {
            runCatching {
                api.recordStart(
                    id = id,
                    bitrate = bitrate,
                    action = "startplay",
                    type = type,
                    startLogTime = playbackTime.startLogTime,
                    alg = alg,
                    sourceId = playbackTime.sourceId,
                    sourceType = playbackTime.sourceType,
                    categoryId = playbackTime.categoryId
                )
            }.onSuccess {
                SDKLogUtil.i("[DataRecorder][recordStart] success, $it")
            }.onFailure {
                SDKLogUtil.e("[DataRecorder][recordStart] failure", it)
            }
        }

    }

    fun recordEnd(musicInfo: MusicInfo, playbackTime: PlaybackTime, end: String) {
        val id = musicInfo.id
        val alg = musicInfo.alg
        val type = if (musicInfo.voiceDTO != null) {
            "dj"
        } else {
            "song"
        }
        val bitrate = if (musicInfo.voiceDTO != null) {
            MusicQualityBitrateValue.LQ_BITRATE.bitrate
        } else {
            MusicQualityBitrateValue.fromQuality(MusicQualityHelper.getCurrentQuality()).bitrate
        }.div(1000)

        SDKLogUtil.i("[DataRecorder][recordEnd] id=$id, playbackTime=${playbackTime}")

        SmartScope.launchDefault {
            runCatching {
                api.recordEnd(
                    id = id,
                    bitrate = bitrate,
                    action = "play",
                    end = end,
                    type = type,
                    time = musicInfo.duration.toDouble() / 1000,
                    startLogTime = playbackTime.startLogTime,
                    alg = alg,
                    sourceId = playbackTime.sourceId,
                    sourceType = playbackTime.sourceType,
                    categoryId = playbackTime.categoryId
                )
            }.onSuccess {
                SDKLogUtil.i("[DataRecorder][recordEnd] success, $it")
            }.onFailure {
                SDKLogUtil.e("[DataRecorder][recordEnd] failure", it)
            }
        }
    }

}