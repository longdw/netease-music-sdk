package com.smart.neteasemusic.sdk.data.song

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.common.MusicConst
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue
import com.smart.neteasemusic.sdk.data.bean.MusicQualityDataDTO
import com.smart.neteasemusic.sdk.data.util.MusicQualityBitrateUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author david.long 2026/2/11 15:25
 */
class SongApiImpl : BaseApiImpl() {

    @JsonClass(generateAdapter = true)
    @Keep
    data class HighMusicParams(
        @field:Json(name = "effects") val effects: String?,
        @field:Json(name = "level") val level: String?,
        @field:Json(name = "immerseType") val immerseType: String?,
        @field:Json(name = "realLevel") val realLevel: String?
    )

    private val api by lazy {
        NeRetrofit.getRetrofit().create(SongApi::class.java)
    }

    internal val collectMusicStateFlow = MutableStateFlow<State<Pair<Boolean, MusicInfo>>?>(null)

    /**
     * 获取歌曲详情
     *
     * @param songId 歌曲Id
     * @param withUrl 是否需要播放url
     * @param bitrate 播放码率：128、192、320、999，默认获取用户可收听的最大码率
     * @param extFlags 表示是否获取高品质音源，支持传入 {"hqScene":"normal"}
     * @return MusicInfo? 歌曲详情信息，失败时返回null
     *
     * @see MusicConst.MusicQualityLevel 音质level值
     */
    suspend fun fetchMusicInfo(
        songId: String,
        withUrl: Boolean = true,
        bitrate: Int = MusicQualityBitrateUtil.getPlayQualityBitrateParam(),
        extFlags: String? = null
    ) = try {
        SDKLogUtil.i(
            """
            [SongApiImpl][fetchMusicInfo]
            fetchMusicInfo():
            songId = $songId
            withUrl = $withUrl
            bitrate = $bitrate
            extFlags = $extFlags
        """.trimIndent()
        )
        val highMusicParams = convert2HighMusicParams(
            bitrate = bitrate, level = MusicQualityBitrateValue.fromBitrate(bitrate * 1000).level
        )
        SDKLogUtil.i("[SongApiImpl][fetchMusicInfo] highMusicParams=$highMusicParams")

        val pEffects = highMusicParams.effects
        val pLevel = highMusicParams.level
        val pImmerseType = highMusicParams.immerseType
        val pRealLevel = highMusicParams.realLevel
        val result = api.fetchMusicInfoDetail(
            songId, withUrl, bitrate, pEffects, pLevel, pImmerseType, pRealLevel, extFlags
        )
        SDKLogUtil.i("[SongApiImpl][fetchMusicInfo] result==>$result")
        if (result.isSuccess()) {
            result.data
//            result.data?.also {
//                NeSdk.launchIO { fixMusicInfoLiked(it) }
//            }
        } else {
            null
        }
    } catch (e: Exception) {
        SDKLogUtil.e("[SongApiImpl][fetchMusicInfo] error", e)
        null
    }

    /**
     * 将音频参数转换为高品质音乐参数
     *
     * 根据输入的音效、码率和音质等级，结合设备配置和NPU能力，生成适配的高品质音乐参数。
     *
     * @param effects 音效参数，若为空且满足杜比条件，将使用SDK配置的杜比音效
     * @param bitrate 音频码率，用于确定音质等级
     * @param level 音质等级，默认为空字符串。若为空将根据播放品质或码率自动判断
     * @return [HighMusicParams] 包含处理后的音效、音质等级、沉浸声类型和实际音质等级的参数对象
     */
    @Keep
    fun convert2HighMusicParams(
        effects: String? = "",
        bitrate: Int?,
        level: String? = ""
    ): HighMusicParams {
        var pEffects = effects
        var pLevel = level
        var pImmerseType = ""
        var pRealLevel = ""
//        if (pEffects.isNullOrEmpty()) {
//            pEffects =
//                if (AudioQualityUtils.getPlayQualityBitrateParam() == AudioBitrateValue.DOLBY_BITRATE.bitrate / 1000
//                    || bitrate == AudioBitrateValue.DOLBY_BITRATE.bitrate / 1000
//                ) {
//                    CmIotSDKContext.getSdkConfig()?.dolbyEffects ?: ""
//                } else {
//                    ""
//                }
//        }

        if (pLevel.isNullOrEmpty()) {
            pLevel = when (MusicQualityBitrateUtil.getPlayQualityValue()) {
                MusicQualityBitrateValue.DOLBY_BITRATE.qualityValue -> MusicConst.MusicQualityLevel.AQ_DOLBY
                MusicQualityBitrateValue.EFFECT_BITRATE.qualityValue -> MusicConst.MusicQualityLevel.AQ_JY_EFFECT
                MusicQualityBitrateValue.SKY_BITRATE.qualityValue -> MusicConst.MusicQualityLevel.AQ_IMMERSIVE
                MusicQualityBitrateValue.MASTER_BITRATE.qualityValue -> MusicConst.MusicQualityLevel.AQ_JY_MASTER
                MusicQualityBitrateValue.VIVID_BITRATE.qualityValue -> MusicConst.MusicQualityLevel.AQ_VIVID
                else -> ""
            }
            if (pLevel.isEmpty()) {
                pLevel = when (bitrate) {
                    MusicQualityBitrateValue.DOLBY_BITRATE.bitrate / 1000 -> MusicConst.MusicQualityLevel.AQ_DOLBY
                    MusicQualityBitrateValue.EFFECT_BITRATE.bitrate / 1000 -> MusicConst.MusicQualityLevel.AQ_JY_EFFECT
                    MusicQualityBitrateValue.SKY_BITRATE.bitrate / 1000 -> MusicConst.MusicQualityLevel.AQ_IMMERSIVE
                    MusicQualityBitrateValue.MASTER_BITRATE.bitrate / 1000 -> MusicConst.MusicQualityLevel.AQ_JY_MASTER
                    MusicQualityBitrateValue.VIVID_BITRATE.bitrate / 1000 -> MusicConst.MusicQualityLevel.AQ_VIVID
                    else -> ""
                }
            }
        }
//        //这里后面可以考虑配置
//        var skyChannel: SkySoundChannelValue = SkySoundChannelValue.AAC
//        pImmerseType = when (pLevel) {
//            MusicConst.MusicQualityLevel.AQ_IMMERSIVE -> {
//                when (skyChannel) {
//                    SkySoundChannelValue.AAC -> {
//                        "aac"
//                    }
//                    SkySoundChannelValue.C51 -> {
//                        "c51"
//                    }
//                    SkySoundChannelValue.STE -> {
//                        "ste"
//                    }
//                    else -> {
//                        "aac"
//                    }
//                }
//            }
//            else -> ""
//        }

//        when (pLevel) {
//            MusicConst.MusicQualityLevel.AQ_JY_EFFECT -> {
//                // 配置不请求原始音频
//                pRealLevel = MusicConst.MusicQualityLevel.AQ_JY_EFFECT
//            }
//
//            MusicConst.MusicQualityLevel.AQ_JY_MASTER -> {
//                // 配置不请求原始音频
//                pRealLevel = MusicConst.MusicQualityLevel.AQ_JY_MASTER
//            }
//            MusicConst.MusicQualityLevel.AQ_DOLBY -> {
//                if (SDKContext.sdkConfig?.supportDolby == false) {
//                    pLevel = MusicConst.MusicQualityLevel.AQ_LOSSLESS
//                    pRealLevel = MusicConst.MusicQualityLevel.AQ_LOSSLESS
//                    pEffects = ""
//
//                }
//            }
//        }

        return HighMusicParams(pEffects, pLevel, pImmerseType, pRealLevel)
    }

    /**
     * 获取歌曲音质列表
     */
    suspend fun getAudioQualityList(songId: String): MusicQualityDataDTO? {
        val state = startFetch {
            api.getMusicQualityList(songId)
        }
        return if (state is SUCCESS) {
            state.data
        } else {
            null
        }
    }

    /**
     * 添加或删除歌曲到'我喜欢的音乐'歌单
     */
    suspend fun operMusicOfMyLikePlayList(
        isLike: Boolean,
        musicInfo: MusicInfo
    ): State<Pair<Boolean, String>> {
        val state = runCatching {
            api.operMusicOfMyLikePlayList(isLike, musicInfo.id)
        }.fold(
            onSuccess =  { result ->
                if (result.isSuccess()) {
                    val pair = Pair(isLike, musicInfo)
                    musicInfo.isLike = isLike
                    collectMusicStateFlow.emit(State.success(pair))
                    success(Pair(isLike, musicInfo.id))
                } else {
                    collectMusicStateFlow.emit(State.error(result.exception, result.code, result.message))
                    error(IllegalArgumentException("operMusicOfMyLikePlayList(): error, errorCode=${result.code}"))
                }
            },

            onFailure = {
                error(it)
            }
        )
        return state
    }

}