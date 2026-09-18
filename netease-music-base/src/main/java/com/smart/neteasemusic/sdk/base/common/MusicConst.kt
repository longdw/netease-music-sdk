package com.smart.neteasemusic.sdk.base.common

/**
 * @author david.long 2026/1/14 09:37
 */
object MusicConst {
    const val KSONGMERGE_BITRATE: Int = 512000
    const val LQ_BITRATE: Int = 128000
    const val VIVID_BITRATE: Int = 5999000
    const val JY_MASTER_BITRATE: Int = 4999000
    const val JY_EFFECT_BITRATE: Int = 3999000
    const val DOLBY_BITRATE: Int = 2999000
    const val IMMERSE_BITRATE: Int = 4099000 // 沉浸声2.0
    const val HIRES_BITRATE: Int = 1999000
    const val SQ_BITRATE: Int = 999000
    const val MQ_BITRATE: Int = 192000
    const val HQ_BITRATE: Int = 320000

    object MusicInfoConst {
        const val SQ_BITRATE_THRESHOLD: Int = 400000
        const val MQ_BITRATE_THRESHOLD: Int = 160000
        const val HQ_MUSIC_BITRATE_THRESHOLD: Int = 300000
        const val HQ_MUSIC_AAC_BITRATE: Int = 250000
    }

    /**
     * 音质等级，为了方便拓展，改为String类型枚举
     */
    object MusicQualityLevel {
        const val AQ_VIVID: String = "vivid"
        const val AQ_JY_EFFECT: String = "jyeffect" //高清臻音
        const val AQ_JY_MASTER: String = "jymaster" //鲸云母带
        const val AQ_DOLBY: String = "dolby" //杜比

        const val AQ_IMMERSIVE: String = "sky" //沉浸声2.0
        const val AQ_HIRES: String = "hires" //hires音质
        const val AQ_LOSSLESS: String = "lossless" //无损，对应原来的999000
        const val AQ_KSONGMERGE: String = "ksmerge" //单个文件的K歌歌曲，对应512000
        const val AQ_EXHIGH: String = "exhigh" //极高，对应原来的320000
        const val AQ_HIGHER: String = "higher" //较高，对应原来的192000
        const val AQ_STANDARD: String = "standard" //标准，对应原来的128000
        const val AQ_64ACC: String = "64aac" //64acc，对应原来的64000
        const val AQ_AUTO: String = "auto" //autio，对应原来的0

        val UPGRADE_DEFAULT_LEVEL: String = AQ_EXHIGH //默认升级level
    }
}