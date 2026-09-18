package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/18 14:06
 */
@JsonClass(generateAdapter = true)

data class MusicInfoDetailDTO(
    /**歌曲id*/
    @field:Json(name = "id") val id: String,
    /**歌曲名称*/
    @field:Json(name = "name") val name: String? = null,
    /**歌曲时长**/
    @field:Json(name = "duration") var duration: Long = 0L,
    /** 专辑名称 */
    @field:Json(name = "albumName") var albumName:String? = null,
    /** 专辑Id */
    @field:Json(name = "albumId") var albumId:String? = null,

    /** 专辑艺人Id */
    @field:Json(name = "albumArtistId") var albumArtistId:String? = null,
    /** 专辑艺人名 */
    @field:Json(name = "albumArtistName") var albumArtistName:String? = null,

    /** 艺人Id */
    @field:Json(name = "artistId") var artistId:String? = null,
    /** 艺人名 */
    @field:Json(name = "artistName") var artistName:String? = null,

    /** mvId */
    @field:Json(name = "mvId") var mvId:String? = null,

    /**是否下载歌曲（true，false）**/
    @field:Json(name = "downloadFlag") val downloadFlag: Boolean = false,
    /**是否需要付费才能播放（true、false）*/
    @field:Json(name = "payPlayFlag") val payPlayFlag: Boolean = false,
    /**是否需要付费才能下载（true、false）**/
    @field:Json(name = "payDownloadFlag") val payDownloadFlag: Boolean = false,
    /**需要VIP才能播放和下载*/
    @field:Json(name = "vipFlag") val vipFlag: Boolean = false,
    /** 是否喜欢 */
    @field:Json(name = "liked") val liked: Boolean = false,
    /** 歌曲原本的最大码率 */
    @field:Json(name = "songMaxBr") val songMaxBr: Int? = 0,
    /**用户能播放的最大码率*/
    @field:Json(name = "userMaxBr") val userMaxBr: Int? = 0,
    /** 歌曲码率对应的level */
    @field:Json(name = "maxBrLevel") val maxBrLevel: String? = null,

    /**
     * 用户对当前歌曲最高可播放音质
     * 根据歌曲最大可播放码率以及用户音质权益决定
     *
     * Notes: 如果歌曲没有算法音质，则按照用户最大可播放码率寻找音质，当最大可下载码率为999000，且歌曲存在hires音质，则优先返回hires音质
     */
    @field:Json(name = "plLevel") val plLevel: String? = null,
    /** 用户下载歌曲码率对应的level */
    @field:Json(name = "dlLevel") val dlLevel: String? = null,
    /** 歌曲的大小 */
    @field:Json(name = "songSize") var songSize: Int? = 0,
    /** 歌曲的MD5 */
    @field:Json(name = "songMd5") val songMd5: String? = "",
    /** 歌曲的曲风标签 */
    @field:Json(name = "songTag") val songTag: List<String>? = listOf(),
    /** 歌曲付费类型 */
    @field:Json(name = "songFee") val songFee: Int? = 0,
    /** 返回的歌曲码率 码率定义参考MusicQualityBitrateValue */
    @field:Json(name = "br") var br: Long? = 0L,
    /**是否有杜比：1*/
    @field:Json(name = "audioFlag") val audioFlag: Int? = 0,
    /**音效信息（目前仅杜比才有）*/
    @field:Json(name = "effects") val effects: String? = null,
    /**是否有版权（true，false）*/
    @field:Json(name = "visible") val visible: Boolean = false,


)
