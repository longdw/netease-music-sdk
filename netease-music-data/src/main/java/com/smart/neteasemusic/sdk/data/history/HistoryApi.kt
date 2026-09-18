package com.smart.neteasemusic.sdk.data.history

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.HistoryAlbumListDTO
import com.smart.neteasemusic.sdk.data.bean.HistoryMusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.HistoryPlayListDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastPageDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/4/22 10:59
 */
interface HistoryApi {
    /**
     * 获取最近播放歌单列表
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/play/record/list")
    suspend fun fetchPlaylistHistory(
        @Field("limit") limit: Int = 100
    ): ApiResult<HistoryPlayListDTO>

    /**
     * 获取最近播放专辑列表
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/album/play/record/list")
    suspend fun fetchAlbumHistory(
        @Field("limit") limit: Int = 100
    ): ApiResult<HistoryAlbumListDTO>

    @FormUrlEncoded
    @POST("/openapi/djradio/play/progress/record/list")
    suspend fun fetchPodcastHistory(
        @Field("limit") limit: Int = 100,
        @Field("filterVoiceBook") filterVoiceBook : Boolean = false
    ): ApiResult<PodcastPageDTO>

    /**
     * 获取最近播放歌曲列表
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/play/record/list")
    suspend fun fetchMusicHistory(
        @Field("limit") limit: Int = 300,
        @Field("qualityFlag") qualityFlag: Boolean = false
    ): ApiResult<HistoryMusicInfoDTO>
}