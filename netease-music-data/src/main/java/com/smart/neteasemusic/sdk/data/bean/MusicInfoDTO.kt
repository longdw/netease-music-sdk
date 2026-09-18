package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.bean.FreeTrail
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 音乐类网络数据模型
 * @author david.long 2026/2/5 10:31
 */
@JsonClass(generateAdapter = true)
@Keep
data class MusicInfoDTO(
    /**歌曲id*/
    @field:Json(name = "id") val id: String,
    /**歌曲名称*/
    @field:Json(name = "name") val name: String? = null,
    /**歌曲时长**/
    @field:Json(name = "duration") var duration: Long = 0L,
    /**是否有版权（true，false）*/
    @field:Json(name = "visible") val visible: Boolean = false,
    /**
     * 是否可以播放
     */
    @field:Json(name = "playFlag") var playFlag: Boolean = false,
    /**是否下载歌曲（true，false）**/
    @field:Json(name = "downloadFlag") val downloadFlag: Boolean = false,
    /**是否需要付费才能播放（true、false）*/
    @field:Json(name = "payPlayFlag") val payPlayFlag: Boolean = false,
    /**是否需要付费才能下载（true、false）**/
    @field:Json(name = "payDownloadFlag") val payDownloadFlag: Boolean = false,
    /**专辑艺人Id*/
    @field:Json(name = "albumArtistId") val albumArtistId: String? = null,
    /**专辑艺人名*/
    @field:Json(name = "albumArtistName") val albumArtistName: String? = null,
    /**专辑信息**/
    @field:Json(name = "album") val album: AlbumDTO? = null,
    /** 专辑名 */
    @field:Json(name = "albumName") val albumName: String? = null,
    /** 专辑id */
    @field:Json(name = "albumId") val albumId: String? = null,
    /**艺人Id*/
    @field:Json(name = "artistId") val artistId: String? = null,
    /**艺人名**/
    @field:Json(name = "artistName") val artistName: String? = null,
    /**艺人列表*/
    @field:Json(name = "artists") val artists: List<ArtistDTO>? = listOf(),
    /**完整艺人列表（包含已下线艺人）*/
    @field:Json(name = "fullArtists") val fullArtists: List<ArtistDTO>? = listOf(),
    /**
     * 封面图片的原始 URL, 建议根据实际需求处理图片尺寸。
     *
     * 可以通过在 URL 后添加参数来获取不同尺寸的图片，支持的参数包括：
     * - `?param=宽y高`：指定图片尺寸，如 `?param=200y200`
     * - `?param=最长边`：保持比例缩放，如 `?param=200`
     * - `?quality=质量`：压缩质量，范围 1-100
     *
     * 示例：
     * ```
     * 原始URL：http://example.com/image.jpg
     * 缩略图：http://example.com/image.jpg?param=200y200
     * 压缩图：http://example.com/image.jpg?param=200y200&quality=75
     * ```
     *
     * 更多详细信息请参考[网易云图片服务文档](https://sf.163.com/help/documents/66982522786074624)
     */
    @field:Json(name = "coverImgUrl") val coverImgUrl: String? = null,
    /**播放url*/
    @field:Json(name = "playUrl") var playUrl: String? = null,
    /**播放url过期时间*/
    @field:Json(name = "playUrlExpireTime") var playUrlExpireTime: Long? = null,
    /**mvId*/
    @field:Json(name = "mvId") val mvId: String? = null,
    /**是否vip歌曲*/
    @field:Json(name = "vipFlag") val vipFlag: Boolean = false,
    /** 需要VIP才能播放 */
    @field:Json(name = "vipPlayFlag") val vipPlayFlag: Boolean = false,
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
    /**
     * 物料码率等级
     * 根据br转换
     */
    @field:Json(name = "level") var level: String? = null,
    /**
     * level混淆的场景
     * 1:NPU超分
     */
    @field:Json(name = "levelConfuse") var levelConfuse: Int? = null,
    /** 歌曲的大小 */
    @field:Json(name = "songSize") var songSize: Int? = 0,
    /** 歌曲的MD5 */
    @field:Json(name = "songMd5") val songMd5: String? = "",
    /** 歌曲的曲风标签 */
    @field:Json(name = "songTag") val songTag: List<String>? = listOf(),
    /** 是否喜欢 */
    @field:Json(name = "liked") val liked: Boolean = false,
    /** 歌曲付费类型 */
    @field:Json(name = "songFee") val songFee: Int? = 0,
    /** 算法推荐使用，播放数据上报接口需透传该字段 */
    @field:Json(name = "alg") val alg: String? = null,
    /** 返回的歌曲码率 码率定义参考MusicQualityBitrateValue */
    @field:Json(name = "br") var br: Long? = 0L,
    /**是否有杜比：1*/
    @field:Json(name = "audioFlag") val audioFlag: Int? = 0,
    /**音效信息（目前仅杜比才有）*/
    @field:Json(name = "effects") val effects: String? = null,
    /**试听权限信息*/
    @field:Json(name = "freeTrialPrivilege") val freeTrialPrivilege: FreeTrialPrivilegeDTO? = null,
    /**是否支持试听*/
    @field:Json(name = "freeTrailFlag") var freeTrailFlag: Boolean = false,
    /**试听起始位置*/
    @field:Json(name = "freeTrail") var freeTrail: FreeTrail? = null,
    /** 音质列表 */
    @field:Json(name = "qualities") val qualities: List<String>? = listOf(),
    /** 云盘歌曲 */
    @field:Json(name = "privateCloudSong") val privateCloudSong: Boolean = false,
    /** 音量均衡增益 **/
    @field:Json(name = "gain") var gain: Float? = 0F,
    /** 推荐理由（私人定制接口）*/
    @field:Json(name = "extMap") val extMap: Map<String, Any?>? = null,
) {
    /** vip歌曲 是否可播放 vip歌曲且用户非vip就不能播 */
    fun isCanVipPlay(): Boolean {
        return vipPlayFlag && UserManager.isVip()
    }

    /**
     * 是否是vip歌曲
     */
    fun isVipSong(): Boolean {
        return vipPlayFlag
    }

    /** 非vip 需要单曲付费 */
    fun needPaySingle(): Boolean {
        return !playFlag && !vipPlayFlag
    }

    fun getArtistsName(): String {
        if (!artistName.isNullOrBlank()) {
            return artistName
        }
        return if (artists.isNullOrEmpty()) {
            "未知歌手"
        } else {
            artists.joinToString { it.name.toString() }
        }
    }

    fun toMusicInfo() = MusicInfo(
        id = id,
        isCanPlay = playFlag || freeTrailFlag,
        isCanTrail = !playFlag && freeTrailFlag,
        playUrl = playUrl ?: "",
        coverUrl = coverImgUrl ?: "",
        name = name ?: "",
        artistName = getArtistsName(),
        duration = duration,
        hasCopyRight = visible,
        freeTrail = freeTrail,
        folderId = album?.id ?: albumId,
        folderName = album?.name ?: albumName,
        br = br,
        level = level,
        playUrlExpireTime = playUrlExpireTime,
        vipFlag = vipFlag,
        vipPlayFlag = vipPlayFlag,
        isLike = liked,
        alg = alg,
        maxBrLevel = maxBrLevel
    )
}