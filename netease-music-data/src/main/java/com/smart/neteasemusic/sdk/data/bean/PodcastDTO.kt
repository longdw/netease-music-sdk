package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.bean.FreeTrail
import com.smart.neteasemusic.sdk.data.common.LimitReason
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable
import kotlin.math.roundToInt

/**
 * @author david.long 2026/1/14 09:22
 */
@JsonClass(generateAdapter = true)
@Keep
data class PodcastPageDTO(
    @field:Json(name = "total") val total: Long,
    @field:Json(name = "list") val list: List<PodcastDTO>,
    @field:Json(name = "playRecordVoiceDetailVo") val playRecordVoiceDetailVo: VoiceDTO? = null,
)

/**
 * 播单数据结构
 * created by chenchiyi on 2022/3/18 17:19
 *
 * @property id 播单id
 * @property name 播单名称
 * @property picUrl 播单封面url
 * @property createTime 创建时间（时间戳）
 * @property programCount 节目数
 * @property playCount 播放次数
 */
@JsonClass(generateAdapter = true)
@Keep
data class PodcastDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "picUrl") val picUrl: String?,
    @field:Json(name = "createTime") val createTime: Long,
    @field:Json(name = "programCount") val programCount: Long,
    @field:Json(name = "playCount") val playCount: Long,
    @field:Json(name = "category") val category: String? = "",
    @field:Json(name = "categoryId") val categoryId: Long = 0,
    @field:Json(name = "secondCategory") val secondCategory: String? = "",
    @field:Json(name = "secondCategoryId") val secondCategoryId: Long = 0,
    @field:Json(name = "lastVoiceVO") var lastVoiceDTO: VoiceDTO? = null,
    @field:Json(name = "lastVoiceId") val lastVoiceId: String? = "",
    @field:Json(name = "djName") val djName: String? = "",
    @field:Json(name = "desc") val desc: String? = "",
    @field:Json(name = "subscribe") val subscribe: Boolean = false,
    @field:Json(name = "fees") val fees: List<FeeDTO>? = listOf(),
    @field:Json(name = "feeActivity") val feeActivity: PodcastFeeActivityDTO? = null,
) : Serializable

@JsonClass(generateAdapter = true)
@Keep
data class DJRadioDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "picUrl") val picUrl: String? = null,
    @field:Json(name = "createTime") val createTime: Long = 0,
    @field:Json(name = "programCount") val programCount: Long = 0,
    @field:Json(name = "playCount") val playCount: Long = 0,
    @field:Json(name = "category") val category: String? = "",
    @field:Json(name = "categoryId") val categoryId: Long = 0,
    @field:Json(name = "secondCategory") val secondCategory: String? = "",
    @field:Json(name = "secondCategoryId") val secondCategoryId: Long = 0,
    @field:Json(name = "djName") val djName: String? = "",
    @field:Json(name = "desc") val desc: String? = "",
) {
    fun convertToPodcast(): PodcastDTO {
        return PodcastDTO(
            id = id,
            name = name,
            picUrl = picUrl,
            createTime = createTime,
            programCount = programCount,
            playCount = playCount,
            category = category,
            categoryId = categoryId,
            secondCategory = secondCategory,
            secondCategoryId = secondCategoryId,
            djName = djName,
            desc = desc,
        )
    }
}

/**
 * 播客付费模型
 */
@JsonClass(generateAdapter = true)
@Keep
data class FeeDTO(
    @field:Json(name = "feeType") val feeType: Int,
    @field:Json(name = "price") val price: String? = "",
)

/**
 * 播客付费模型
 */
@JsonClass(generateAdapter = true)
@Keep
data class PodcastFeeActivityDTO(
    @field:Json(name = "limitFreeStartTime") val limitFreeStartTime: Long,
    @field:Json(name = "limitFreeEndTime") val limitFreeEndTime: Long,
)

/**
 * 声音付费模型
 */
@JsonClass(generateAdapter = true)
@Keep
data class VoiceFeeActivityDTO(
    @field:Json(name = "limitFreeStartTime") val limitFreeStartTime: Long,
    @field:Json(name = "limitFreeEndTime") val limitFreeEndTime: Long,
    @field:Json(name = "freeTry") val freeTry: Boolean,
    @field:Json(name = "freeTryStartTime") val freeTryStartTime: Long,
    @field:Json(name = "freeTryEndTime") val freeTryEndTime: Long,
)

@JsonClass(generateAdapter = true)
@Keep
data class PodcastListDTO(
    @field:Json(name = "total") val total: Long,
    @field:Json(name = "list") val list: List<PodcastDTO>,
)

/**
 * 收藏的播客
 *
 * @property records 播单列表
 * @property page 分页信息
 * @property count 播单总数
 */
@JsonClass(generateAdapter = true)
@Keep
data class MyCollectPodcastDTO(
    @field:Json(name = "records") val records: List<PodcastDTO>,
    @field:Json(name = "page") val page: PageDTO?,
    @field:Json(name = "count") val count: Long,
) : java.io.Serializable {
    companion object {
        private const val serialVersionUID: Long = -95830537L
    }

}

/**
 * 声音资源（无url）
 *
 * @property id 声音Id
 * @property name 声音名称
 * @property coverUrl 封面url
 * @property playCount 播放次数
 * @property description 描述
 * @property trackId 音频Id，用来获取播单地址
 * @property duration 播放时长
 * @property createTime 创建时间（时间戳）
 * @property encVoiceId 加密的声音id
 * @property serialNum 加密的声音id
 */
@JsonClass(generateAdapter = true)
@Keep
data class VoiceDTO(
    @field:Json(name = "id") val id: String? = "",
    @field:Json(name = "radioId") val radioId: Long = 0L,
    @field:Json(name = "podcastId") val podcastId: Long = 0L,
    @field:Json(name = "radioName") val radioName: String? = "",
    @field:Json(name = "name") val name: String? = "",
    @field:Json(name = "coverUrl") var coverUrl: String?,
    @field:Json(name = "playCount") val playCount: Long = 0L,
    @field:Json(name = "description") val description: String? = "",
    @field:Json(name = "trackId") val trackId: String? = "",
    @field:Json(name = "duration") var duration: Long = 0L,
    @field:Json(name = "createTime") val createTime: Long = 0L,
    @field:Json(name = "encVoiceId") val encVoiceId: String? = "",
    @field:Json(name = "serialNum") val serialNum: Long = 0L,
    @field:Json(name = "audioUrl") var audioUrlV1: String? = "",
    @field:Json(name = "djradio") var djradio: DJRadioDTO? = null,
    @field:Json(name = "djName") var djName: String? = "",
    @field:Json(name = "fees") var fees: List<FeeDTO>? = listOf(),
    @field:Json(name = "feeActivity") var feeActivity: VoiceFeeActivityDTO? = null,
    @field:Json(name = "playRecordVO") var privatePlayRecord: PlayRecordDTO? = null,
    @field:Json(name = "playRecordAllWhereVo") var privatePlayRecordAllWhere: DJPlayRecordAllWhereDTO? = null,
    @field:Json(name = "playFlag") var playFlag: String? = null,
    @param:Json(ignore = true) var audioSize: Long = 0,
    @param:Json(ignore = true) var freeTrail: FreeTrail? = null,
) : Serializable {

    /**
     * 设置全端播放记录
     * @param record 播放记录对象
     */
    fun setPlayRecord(record: DJPlayRecordAllWhereDTO) {
        this.privatePlayRecordAllWhere = record
    }

    /**
     * 获取最新的播放记录
     *
     * 比较本地记录和全端记录，返回最新的一条。
     * 如果两条记录都存在，根据播放时间戳选择最新的一条
     *
     * @return DJPlayRecordAllWhereVo? 最新的播放记录，可能为空
     */
    fun getPlayRecord(): DJPlayRecordAllWhereDTO? {
        val recordAllWhere = privatePlayRecordAllWhere
        val record = privatePlayRecord?.let {
            DJPlayRecordAllWhereDTO(
                pvd = (it.listenLocation / 1000.0).roundToInt(), // 单位: 秒
                resourceId = it.id,
                os = null,
                playTime = it.uploadTime,
                subResourceId = null,
                resourceType = "VOICE"
            )
        }

        return when {
            record != null && recordAllWhere != null -> {
                // 如果两个记录都有playTime，进行比较
                val recordPlayTime = record.playTime ?: Long.MIN_VALUE
                val recordAllWherePlayTime = recordAllWhere.playTime ?: Long.MIN_VALUE

                if (recordPlayTime > recordAllWherePlayTime) {
                    record
                } else {
                    recordAllWhere
                }
            }

            recordAllWhere != null -> recordAllWhere
            record != null -> record
            else -> null // 两者都是null
        }
    }

    /**
     * 获取声音所属的播单ID
     * 优先级：radioId > podcastId > djradio.id
     *
     * @return Long? 播单ID，可能为空
     */
    fun getSourceId(): Long? {
        if (radioId > 0L) return radioId
        if (podcastId > 0L) return podcastId
        return djradio?.id
    }

    /**
     * 获取播放限制原因
     * @return 播放限制原因，null 表示可以播放
     */
    fun getLimitReason(): String? = when {
        !playFlag.isNullOrEmpty() -> LimitReason.getLimitReason(playFlag)
        else -> null
    }

    /**
     * VoiceDTO 转换为 MusicInfo
     */
    fun toMusicInfo() = MusicInfo(
        id = id ?: "",
        isCanPlay = playFlag.isNullOrEmpty(),
        playUrl = audioUrlV1 ?: "",
        coverUrl = coverUrl ?: "",
        name = name ?: "",
        artistName = djradio?.name ?: "",
        duration = duration,
        hasCopyRight = true,
        voiceDTO = this,
        playCount = playCount
    )

    override fun toString(): String {
        return "VoiceDTO(id=$id, radioId=$radioId, podcastId=$podcastId, radioName=$radioName, name=$name, playCount=$playCount, trackId=$trackId, duration=$duration, encVoiceId=$encVoiceId, privatePlayRecord=$privatePlayRecord, privatePlayRecordAllWhere=$privatePlayRecordAllWhere, audioSize=$audioSize, playFlag=$playFlag, audioUrlV1=$audioUrlV1)"
    }
}

/**
 * 播单下的声音资源列表
 *
 * @property total 声音数量
 * @property list 声音资源列表
 * @property playRecordVoiceDetailVo 最近播放的声音
 *
 */
@JsonClass(generateAdapter = true)
@Keep
data class VoiceListVO(
    @field:Json(name = "total") val total: Long,
    @field:Json(name = "list") val list: List<VoiceDTO>,
    @field:Json(name = "playRecordVoiceDetailVo") val playRecordVoiceDetailVo: VoiceDTO? = null,
) {
    fun createMusicInfoList(voices: List<VoiceDTO>): List<MusicInfo> {
        val items: MutableList<MusicInfo> = mutableListOf()
        voices.forEach(action = {
            items.add(it.toMusicInfo())
        })
        return items
    }
}

/**
 * 猜你喜欢播播单
 * @property id 播单id
 * @property name  资源名字
 * @property picUrl 封面URL
 * @property playCount 播放数
 * @property programCount 声音数
 * @property createTime 创建时间
 * @property desc 简介
 * @property category 一级分类
 * @property secondCategory 二级分类
 * @property djName 主播名字
 * @property subscribe 收藏状态
 */
@JsonClass(generateAdapter = true)
@Keep
data class PodcastLikeDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "picUrl") val picUrl: String?,
    @field:Json(name = "playCount") val playCount: Long = 0L,
    @field:Json(name = "programCount") val programCount: Long = 0L,
    @field:Json(name = "createTime") val createTime: Long = 0L,
    @field:Json(name = "desc") val desc: String = "",
    @field:Json(name = "category") val category: String? = "",
    @field:Json(name = "categoryId") val categoryId: Long = 0,
    @field:Json(name = "secondCategory") val secondCategory: String? = "",
    @field:Json(name = "secondCategoryId") val secondCategoryId: Long = 0,
    @field:Json(name = "djName") val djName: String? = "",
    @field:Json(name = "subscribe") val subscribe: Boolean = false,
)

@JsonClass(generateAdapter = true)
@Keep
data class PlayRecordDTO(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "isListened") val isListened: Boolean = false,
    @field:Json(name = "listenLocation") val listenLocation: Long = 0,
    @field:Json(name = "uploadTime") val uploadTime: Long? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -90000038L
    }
}

/**
 * @property pvd 资源播放进度
 * @property resourceId 资源ID
 * @property playTime 资源播放时间
 * @property resourceType 资源类型
 * @property subResourceId 子资源ID：歌单 -> 歌曲， 专辑 -> 歌曲， 电台 -> 节目
 * @property os 设备类型
 */
@JsonClass(generateAdapter = true)
@Keep
data class DJPlayRecordAllWhereDTO(
    @field:Json(name = "vpd") val pvd: Int? = null,
    @field:Json(name = "resourceId") val resourceId: String? = null,
    @field:Json(name = "playTime") val playTime: Long? = null,
    @field:Json(name = "resourceType") val resourceType: String? = null,
    @field:Json(name = "subResourceId") val subResourceId: String? = null,
    @field:Json(name = "os") val os: String? = null,
)


/**
 * 根据声音id 获取前后指定范围声音列表
 */
@JsonClass(generateAdapter = true)
@Keep
data class VoiceAroundListDTO(
    @field:Json(name = "list") val list: List<VoiceDTO>,
    @field:Json(name = "asc") val asc: Boolean = false,
    @field:Json(name = "upFallsPage") val upFallsPage: FallsPage? = null,
    @field:Json(name = "downFallsPage") val downFallsPage: FallsPage? = null,
    @field:Json(name = "radioId") val radioId: Long = 0,
)

/**
 * 播单列表分页信息
 * @property size 每页数量
 * @property cursor 游标，默认返回本页列表最后一条数据的游标值
 */
@JsonClass(generateAdapter = true)
@Keep
data class FallsPage(
    @field:Json(name = "size") val size: Int = 0,
    @field:Json(name = "cursor") val cursor: String? = "",
    @field:Json(name = "more") val more: Boolean = false,
)

@JsonClass(generateAdapter = true)
@Keep
data class FallsVoiceListDTO(
    @field:Json(name = "extInfo") val extInfo: ProgramListExtVO? = null,
    @field:Json(name = "upFallsPage") val upFallsPage: FallsPage? = null,
    @field:Json(name = "downFallsPage") val downFallsPage: FallsPage? = null,
    @field:Json(name = "records") val records: List<VoiceDTO>? = null,
)

@JsonClass(generateAdapter = true)
@Keep
data class ProgramListExtVO(
    @field:Json(name = "radioData") val radioData: PodcastDetailVO,
    @field:Json(name = "rangeInfo") val rangeInfo: SelectedRangeInfo? = null,
)

@JsonClass(generateAdapter = true)
@Keep
data class SelectedRangeInfo(
    @field:Json(name = "totalCount") val totalCount: Long = 0,
    @field:Json(name = "orderlySnapShotProgramIds") val orderlySnapShotProgramIds: List<Long>? = null,
    @field:Json(name = "selectedRangeList") val selectedRangeList: List<SelectedRange>? = null,
)

@JsonClass(generateAdapter = true)
@Keep
data class SelectedRange(
    @field:Json(name = "startSeqNo") val startSeqNo: Int = 0,
    @field:Json(name = "startProgramId") val startProgramId: Long = 0,
    @field:Json(name = "endSeqNo") val endSeqNo: Int = 0,
    @field:Json(name = "endProgramId") val endProgramId: Long = 0,
)
data class PodcastDetailVO(
    @field:Json(name = "id") val id: String? = "",
    @field:Json(name = "name") val name: String? = "",
    @field:Json(name = "picUrl") val picUrl: String?,
    @field:Json(name = "playCount") val playCount: Long = 0L,
    @field:Json(name = "programCount") val programCount: Long = 0L,
    @field:Json(name = "createTime") val createTime: Long = 0L,
    @field:Json(name = "desc") val desc: String = "",
    @field:Json(name = "categoryId") val categoryId: String = "",
    @field:Json(name = "category") val category: String = "",
    @field:Json(name = "secondCategoryId") val secondCategoryId: String = "",
    @field:Json(name = "secondCategory") val secondCategory: String = "",
    @field:Json(name = "subscribe") val subscribe: Boolean,
)

@JsonClass(generateAdapter = true)
@Keep
data class WeekNewsPodcastDTO(
    @field:Json(name = "blocks") val blocks: List<WeekNewsBlocksDTO>,
    @field:Json(name = "cursor") val cursor: String? = "",
    @field:Json(name = "cursor") val hasMore: Boolean

    )

@JsonClass(generateAdapter = true)
@Keep
data class WeekNewsBlocksDTO(
    @field:Json(name = "blockCode") val blockCode: String? = "",
    @field:Json(name = "blockName") val blockName: String? = "",
    @field:Json(name = "blockResourceNum") val blockResourceNum: Int = 0,
    @field:Json(name = "items") val items: List<WeekNewsBlocksDetailDTO>
)

@JsonClass(generateAdapter = true)
@Keep
data class WeekNewsBlocksDetailDTO(
    @field:Json(name = "resType") val resType: String ,
    @field:Json(name = "id") val id: String,
    @field:Json(name = "encId") val encId: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "subTitle") val subTitle: String,
    @field:Json(name = "coverUrl") val coverUrl: String,
    @field:Json(name = "nickName") val nickName: String,
    @field:Json(name = "trackId") val trackId: String,
    @field:Json(name = "desc") val desc: String? = "",
    @field:Json(name = "icon") val icon: String? = "",
    @field:Json(name = "tags") val tags: String? = "",
    @field:Json(name = "subItems") val subItems: String? = "",
    @field:Json(name = "relatedSongName") val relatedSongName: String? = "",
    @field:Json(name = "algReason") val algReason: String? = "",
    @field:Json(name = "alg") val alg: String? = "",
    @field:Json(name = "ctrp") val ctrp: String? = "",
    @field:Json(name = "playCount") val playCount: Int,
    @field:Json(name = "relatedSongSize") val relatedSongSize: Int,
    @field:Json(name = "duration") val duration: Long,
)