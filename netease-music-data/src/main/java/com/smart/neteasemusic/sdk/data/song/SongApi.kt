package com.smart.neteasemusic.sdk.data.song

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.MusicQualityDataDTO
import com.smart.neteasemusic.sdk.data.util.MusicQualityBitrateUtil
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/1/21 14:09
 */
interface SongApi {
    /**
     * 获取歌曲详情
     *
     * @param songId 歌曲Id
     * @param withUrl 是否需要播放url
     * @param bitrate 播放url的码率：128， 192 ， 320，999 。默认获取用户可收听的最大码率
     * @param effects 支持杜比的音效 DolbyEffectsValue
     * @param level 如音质为高端音质,鲸云母带/鲸云音质/杜比,返回对应的level值 [com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue]
     * @param immerseType 沉浸声音效类型
     * @param extFlags 表示是否获取高品质音源，支持传入 {"hqScene":"normal"}
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/detail/get/v2")
    suspend fun fetchMusicInfoDetail(
        @Field("songId") songId: String,
        @Field("withUrl") withUrl: Boolean,
        @Field("bitrate") bitrate: Int? = MusicQualityBitrateUtil.getPlayQualityBitrateParam(),
        @Field("effects") effects: String? = "",
        @Field("level") level: String? = "",
        @Field("immerseType") immerseType: String? = "",
        @Field("realLevel") realLevel: String? = "",
        @Field("extFlags") extFlags: String? = null
    ): ApiResult<MusicInfoDTO>

    /**
     * 获取歌曲音质
     */
    @POST("/openapi/music/basic/song/music/quality/sound/get")
    @FormUrlEncoded
    suspend fun getMusicQualityList(@Field("songId") songId: String): ApiResult<MusicQualityDataDTO>

    /**
     * 添加或删除歌曲到'我喜欢的音乐'歌单
     */
    @POST("/openapi/music/basic/playlist/song/like/v2")
    @FormUrlEncoded
    suspend fun operMusicOfMyLikePlayList(
        @Field("isLike") isLike: Boolean,
        @Field("songId") songId: String
    ): ApiResult<Any>
}