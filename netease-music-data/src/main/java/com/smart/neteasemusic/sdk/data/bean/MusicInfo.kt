package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smart.neteasemusic.sdk.base.bean.FreeTrail
import com.smart.neteasemusic.sdk.base.common.MusicConst
import com.smart.neteasemusic.sdk.base.util.KSongUtil
import com.smart.neteasemusic.sdk.base.util.MD5Util
import com.smart.neteasemusic.sdk.data.util.MusicQualityBitrateUtil
import com.squareup.moshi.JsonClass

/**
 * 业务模型，抽象了MusicInfoDTO和VoiceDTO，业务侧统一用此类
 * @author david.long 2026/1/14 09:14
 */
@Entity
@JsonClass(generateAdapter = true)
@Keep
data class MusicInfo(
    @PrimaryKey
    /** 歌曲或播客声音id */
    val id: String,
    /** 歌曲名或声音名 */
    val name: String? = null,
    /** 歌曲时长 单位ms */
    val duration: Long = 0L,
    /** 是否有版权（true，false）*/
    val hasCopyRight: Boolean = false,
    /**
     * 物料码率等级
     * 根据br转换
     */
    var level: String? = null,
    /** 艺人名 */
    val artistName: String? = null,
    /** 专辑名或者播单名 */
    val folderName: String? = null,
    /** 专辑id或播单id，注意如果是播单id，其实是一个Long类型 */
    val folderId: String? = null,
    /** 是否收藏 */
    var isLike: Boolean = false,
    /** 是否可以播放（组合字段：支持播放或支持试听就认为可以播放） */
    val isCanPlay: Boolean = false,
    /** 试听起止位置 */
    var freeTrail: FreeTrail? = null,
    /** 是否支持试听 */
    val isCanTrail: Boolean = false,
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
    var coverUrl: String? = null,
    /** 播放量，只有声音有 */
    val playCount: Long = 0L,
    /** 声音音频id TODO 不知道有没有用，没用就删掉 */
    val trackId: String? = null,
    /** 排序号，只有声音有 */
    val serialNum: Int = 0,
    /** 播放url */
    var playUrl: String? = null,
    /** 播放url过期时间 */
    var playUrlExpireTime: Long? = null,
    /** 返回的歌曲码率 码率定义参考[MusicQualityBitrateValue] */
    var br: Long? = 0L,
    /** 是否VIP歌曲 */
    val vipFlag: Boolean = false,
    /** 需要VIP才能播放 */
    val vipPlayFlag: Boolean = false,
    /**播客原始数据**/
    val voiceDTO: VoiceDTO? = null,
    /**
     * 是否支持k歌（随心唱）
     * 一般用来控制随心唱按钮是否能操作
     */
    var supportKSong: Boolean = false,
    /** 算法推荐使用，播放数据上报接口需透传该字段 */
    val alg: String? = null,
    /** 歌曲拥有最大码率对应的level */
    val maxBrLevel: String? = null,
    /** 是否需要付费才能播放 */
    val payPlayFlag: Boolean = false
) {
    /**
     * 获取音频码率
     * 优先级：音质等级(level) > 原始码率(br) > 可用码率(userMaxBr/songMaxBr/savedBr)
     * 特殊情况：K歌模式(+1)、试听模式(+10)
     *
     * @see MusicQualityBitrateUtil.getPlayQualityValue 设置保存的码率
     */
    fun getAudioBitrate(): Long {
        return calculateBitrate(level)
    }

    private fun calculateBitrate(finalLevel: String?): Long {
        // 2. 计算基础码率
        val finalBitrate = if (isKSongAudio()) {
            MusicConst.KSONGMERGE_BITRATE.toLong() // k 歌资源固定码率
        } else {
            if (!finalLevel.isNullOrEmpty()) {
                // 使用音质等级对应的码率，如果转换失败则使用低音质码率
                convertLevelToBitrate(finalLevel) ?: MusicConst.LQ_BITRATE
            } else {
                // 处理原始码率
                var bitrate = br?.toInt() ?: 0
                if (bitrate == 0) {
                    // 原始码率为0时，使用可用码率和保存的音质设置
                    bitrate = MusicQualityBitrateUtil.getPlayQualityBitrate()
                } else {
                    // 标准化码率单位（处理 kbps 和异常大的值）
                    when {
                        bitrate < 10000 -> bitrate *= 1000     // 转换 kbps 到 bps
                        bitrate > 10000000 -> bitrate /= 1000  // 处理异常大的值
                    }
                }

                MusicQualityBitrateUtil.convertRealBitrateToVirtualBitrate(bitrate)
            }.toLong()
        }

        // 3. 应用特殊模式调整
        return when {
            isCanTrail -> finalBitrate + 10L // 试听模式码率微调
            else -> finalBitrate
        }
    }

    private fun convertLevelToBitrate(level: String?): Int? {
        val bitrate = when (level) {
            MusicConst.MusicQualityLevel.AQ_STANDARD -> MusicConst.LQ_BITRATE
            MusicConst.MusicQualityLevel.AQ_HIGHER -> MusicConst.MQ_BITRATE
            MusicConst.MusicQualityLevel.AQ_EXHIGH -> MusicConst.HQ_BITRATE
            MusicConst.MusicQualityLevel.AQ_LOSSLESS -> MusicConst.SQ_BITRATE
            MusicConst.MusicQualityLevel.AQ_HIRES -> MusicConst.HIRES_BITRATE
            MusicConst.MusicQualityLevel.AQ_DOLBY -> MusicConst.DOLBY_BITRATE
            MusicConst.MusicQualityLevel.AQ_JY_EFFECT -> MusicConst.JY_EFFECT_BITRATE
            MusicConst.MusicQualityLevel.AQ_IMMERSIVE -> MusicConst.IMMERSE_BITRATE
            MusicConst.MusicQualityLevel.AQ_JY_MASTER -> MusicConst.JY_MASTER_BITRATE
            MusicConst.MusicQualityLevel.AQ_VIVID -> MusicConst.VIVID_BITRATE
            MusicConst.MusicQualityLevel.AQ_KSONGMERGE -> MusicConst.KSONGMERGE_BITRATE
            else -> null
        }
        return bitrate
    }

    fun getMd5(): String {
        return isCanTrail.toString() + "#" + MD5Util.computeMD5("$id+${getAudioBitrate()}") + "#" + id
    }

    fun getAudioLevel(): String {
        return if (isKSongAudio()) {
            MusicConst.MusicQualityLevel.AQ_KSONGMERGE
        } else {
            level ?: ""
        }
    }

    /**
     * 是否开启随心唱功能，需要打开随心唱按钮&支持随心唱
     *
     * 1、主要控制随心唱相关的按钮是否能操作，比如人声设置按钮
     *
     * 2、当开启随心唱功能时，获取播放url时，根据这个[getAudioBitrate]来拿码率信息
     */
    fun isKSongAudio(): Boolean {
        return KSongUtil.isOpenKSong() && supportKSong
    }
}