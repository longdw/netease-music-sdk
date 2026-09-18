package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.common.MusicConst
import com.smart.neteasemusic.sdk.data.util.MusicQualityBitrateUtil

/**
 * @param bitrate 码率
 * @param level 音质等级
 * 杜比全景声>沉浸环绕声>超清母带>臻音全景声（Audio Vivid）>高清臻音>hires>无损>极高>标准
 * @author david.long 2026/1/14 10:09
 */
@Keep
enum class MusicQualityBitrateValue(
    val bitrate: Int,
    val level: String,
    val readName: String,
    val qualityValue: Int
) {
    /**
     * 自动
     */
    AUTO_BITRATE(0, MusicConst.MusicQualityLevel.AQ_AUTO, "自动", 1),

    /**
     * 普通
     */
    LQ_BITRATE(128000, MusicConst.MusicQualityLevel.AQ_STANDARD, "标准", 2),

    /**
     * 较高
     */
    MQ_BITRATE(192000, MusicConst.MusicQualityLevel.AQ_HIGHER, "较高", 3),

    /**
     * 极高
     */
    HQ_BITRATE(320000, MusicConst.MusicQualityLevel.AQ_EXHIGH, "极高", 4),

    /**
     * 无损
     */
    SQ_BITRATE(999000, MusicConst.MusicQualityLevel.AQ_LOSSLESS, "无损", 5),

    /**
     * HiRes
     */
    HIRES_BITRATE(1999000, MusicConst.MusicQualityLevel.AQ_HIRES, "Hi-Res", 6),

    /**
     * 杜比
     */
    DOLBY_BITRATE(2999000, MusicConst.MusicQualityLevel.AQ_DOLBY, "杜比全景声", 7),

    /**
     * 鲸云臻音
     */
    EFFECT_BITRATE(3999000, MusicConst.MusicQualityLevel.AQ_JY_EFFECT, "高清臻音", 8),

    /**
     * 沉浸声2.0
     */
    SKY_BITRATE(4099000, MusicConst.MusicQualityLevel.AQ_IMMERSIVE, "沉浸环绕声", 9),

    /**
     * 鲸云母带
     */
    MASTER_BITRATE(4999000, MusicConst.MusicQualityLevel.AQ_JY_MASTER, "超清母带", 10),

    /**
     * 臻音全景声 audio vivid
     */
    VIVID_BITRATE(5999000, MusicConst.MusicQualityLevel.AQ_VIVID, "臻音全景声 Audio Vivid", 11),

    /**
     * 这个并非可切换音质，只是在随心唱模式下使用
     */
    KSONG_BITRATE(512000, MusicConst.MusicQualityLevel.AQ_KSONGMERGE, "随心唱", 1000);


    @Keep
    companion object {
        fun fromMusicInfo(musicInfo: MusicInfo): MusicQualityBitrateValue {
            val br =
                MusicQualityBitrateUtil.convertRealBitrateToVirtualBitrate(musicInfo.getAudioBitrate().toInt())
            return fromBitrate(br)
        }

        fun fromBitrate(bitrate: Int) = values().firstOrNull { it.bitrate == bitrate } ?: AUTO_BITRATE

        fun fromBitrate(bitrate: Long) = values().firstOrNull { it.bitrate == bitrate.toInt() } ?: AUTO_BITRATE

        fun fromQuality(qualityValue: Int) = values().firstOrNull { it.qualityValue == qualityValue } ?: AUTO_BITRATE

        fun fromLevel(level: String?) = values().firstOrNull { it.level == level } ?: AUTO_BITRATE
    }
}