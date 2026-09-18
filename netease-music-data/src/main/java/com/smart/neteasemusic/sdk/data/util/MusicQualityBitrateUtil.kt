package com.smart.neteasemusic.sdk.data.util

import com.smart.neteasemusic.sdk.base.common.MusicConst
import com.smart.neteasemusic.sdk.base.common.NeSPConst
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue

/**
 * @author david.long 2026/1/26 13:36
 */
object MusicQualityBitrateUtil {

    /**
     * 只能在api请求入参使用，其余场景使用[MusicQualityBitrateValue]的枚举值
     * 获取码率传参 sp里存储的"在线播放优先音质"对应的码率
     * 0、128、192...
     */
    fun getPlayQualityBitrateParam(): Int {
        return getPlayQualityBitrate() / 1000
    }

    /**
     * 获取sp里存储的"在线播放优先音质"对应的码率：0、128、192...
     */
    @JvmStatic
    fun getPlayQualityBitrate(): Int {
        val quality = getPlayQualityValue()
        return MusicQualityBitrateValue.fromQuality(quality).bitrate
    }

    /**
     * 获取sp里存储的"在线播放优先音质"对应的value：1、2、3...
     */
    @JvmStatic
    fun getPlayQualityValue(): Int {
        // 默认音质 标准音质 LQ_BITRATE
        return SpStorage.getInt(
            NeSPConst.KEY_PLAY_QUALITY,
            MusicQualityBitrateValue.LQ_BITRATE.qualityValue
        )
    }

    /**
     * 真实码率转虚拟码率
     */
    @JvmStatic
    fun convertRealBitrateToVirtualBitrate(realBitrate: Int): Int {
        return when {
            realBitrate >= MusicConst.VIVID_BITRATE -> {
                MusicConst.VIVID_BITRATE
            }
            realBitrate >= MusicConst.JY_MASTER_BITRATE -> {
                MusicConst.JY_MASTER_BITRATE
            }
            realBitrate >= MusicConst.IMMERSE_BITRATE -> {
                MusicConst.IMMERSE_BITRATE
            }
            realBitrate >= MusicConst.JY_EFFECT_BITRATE -> {
                MusicConst.JY_EFFECT_BITRATE
            }
            realBitrate >= MusicConst.DOLBY_BITRATE -> {
                MusicConst.DOLBY_BITRATE
            }
            realBitrate >= MusicConst.HIRES_BITRATE -> {
                MusicConst.HIRES_BITRATE
            }
            realBitrate >= MusicConst.MusicInfoConst.SQ_BITRATE_THRESHOLD -> {
                MusicConst.SQ_BITRATE
            }
            realBitrate >= MusicConst.MusicInfoConst.HQ_MUSIC_AAC_BITRATE -> {
                MusicConst.HQ_BITRATE
            }
            realBitrate >= MusicConst.MusicInfoConst.MQ_BITRATE_THRESHOLD -> {
                MusicConst.MQ_BITRATE
            }
            else -> {
                MusicConst.LQ_BITRATE
            }
        }
    }
}