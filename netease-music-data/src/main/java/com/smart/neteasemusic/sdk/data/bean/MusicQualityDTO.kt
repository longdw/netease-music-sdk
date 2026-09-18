package com.smart.neteasemusic.sdk.data.bean

/**
 * @author david.long 2026/1/29 15:19
 */
data class MusicQualityDataDTO(
    /** 标准 */
    val lmusic: MusicQualityDTO?,
    /** 较高 */
    val mmusic: MusicQualityDTO?,
    /** 极高 */
    val hmusic: MusicQualityDTO?,
    /** 无损 */
    val sqMusic: MusicQualityDTO?,
    /** hi-res */
    val hrMusic: MusicQualityDTO?,
    /** 杜比 */
    val dolbyMusic: MusicQualityDTO?,
    /** 高清臻音 */
    val jyEffectMusic: MusicQualityDTO?,
    /** 超清母带 */
    val jyMasterMusic: MusicQualityDTO?,
    /** 沉浸环绕声 */
    val skMusic: MusicQualityDTO?
)

data class MusicQualityDTO(
    val name: String,
    val id: Long,
    /** 歌曲大小 */
    val size: Long,
    /** 音频格式 */
    val extension: String,
    /** 比特率 */
    val bitrate: Long,
    /** 歌曲时长 */
    val playTime: Long,
    /** 需要的会员类型 */
    val vipTypes: List<String>
)