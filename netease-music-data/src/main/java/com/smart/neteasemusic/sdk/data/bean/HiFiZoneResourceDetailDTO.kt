package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * @author david.long 2026/6/29 9:06
 */
@JsonClass(generateAdapter = true)
@Keep
data class HiFiZoneResourceDetailDTO(
    val resourceType: String? = null,
    val res: Res? = null,
)

@JsonClass(generateAdapter = true)
@Keep
data class Res(
    /** 下面是Banner用到的字段 */
    val id: String? = null,
    val resourceType: String? = null,
    val background: String? = null,
    val orpheus: String? = null,
    val title: String? = null,

    /** 歌单用到的字段 */
    val name: String? = null,
    val coverImgUrl: String? = null,
    val describe: String? = null,
    val creatorNickName: String? = null,
    val creatorAvatarUrl: String? = null,
    val playCount: Long = 0L,
    val subscribedCount: Long = 0L,
    val tags: String? = null,
    val createTime: Long = 0L,
    val subed: Boolean = false,
    val trackCount: Int = 0,
    val specialType: Int = 0,
    val category: String? = null,
    val allFreeTrialFlag: Boolean = false,
    val trackUpdateTime: Long = 0L,
    val creatorId: String? = null,

    /** 歌曲用到的字段 */
    val duration: Long = 0L,
    val artists: List<ArtistDTO>? = listOf(),
    val album: AlbumDTO? = null,
    var playFlag: Boolean = false,
    val downloadFlag: Boolean = false,
    val payPlayFlag: Boolean = false,
    var freeTrailFlag: Boolean = false,
    val payDownloadFlag: Boolean = false,
    val vipFlag: Boolean = false,
    val liked: Boolean = false,
    val vipPlayFlag: Boolean = false,
    /**歌曲原本的最大码率*/
    val songMaxBr: Int? = 0,
    /**用户能播放的最大码率*/
    val userMaxBr: Int? = 0,
    /**歌曲码率对应的level*/
    val maxBrLevel: String? = null,
    /**用户播放歌曲码率对应的level*/
    val plLevel: String? = null,
    /**用户下载歌曲码率对应的level*/
    val dlLevel: String? = null,
    val visible: Boolean = false
)