package com.smart.neteasemusic.sdk.data.playlist

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.MultiPlaylistsDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistTagDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistsFromTagDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/1/28 18:43
 */
interface PlaylistApi {
    /**
     * 获取歌单标签
     */
    @POST("/openapi/music/basic/playlist/tag/get/v2")
    suspend fun fetchPlaylistTags(): ApiResult<List<PlaylistTagDTO>>

    /**
     * 根据标签获取歌单
     * Params:
     * tag - 标签名称
     * limit - 请求个数，默认值10,建议30
     * offset - 请求偏移，默认值0
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/oftag/get/v2")
    suspend fun fetchPlaylistFromTag(
        @Field("tag") tag: String,
        @Field("limit") limit: Int = 10,
        @Field("offset") offset: Int = 0
    ): ApiResult<PlaylistsFromTagDTO>

    /**
     * 获取歌单的详细信息
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/detail/get/v2")
    suspend fun fetchPlaylistDetail(
        @Field("playlistId") playlistId: String
    ): ApiResult<PlaylistDTO>


    /**
     * 获取歌单里的歌曲列表
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/song/list/get/v3")
    suspend fun fetchPlaylistSong(
        @Field("playlistId") playlistId: String,
        @Field("limit") limit: Int = 500,
        @Field("offset") offset: Int = 0,
        @Field("qualityFlag") qualityFlag: Boolean = true
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取红心歌单
     */
    @POST("/openapi/music/basic/playlist/star/get/v2")
    suspend fun fetchMyLikePlaylist(): ApiResult<PlaylistDTO>

    /**
     * 收藏歌单
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/sub/v2")
    suspend fun subscribePlaylist(
        @Field("id") id: String
    ): ApiResult<Boolean>

    /**
     * 取消收藏歌单
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/unsub/v2")
    suspend fun unsubscribePlaylist(
        @Field("id") id: String
    ): ApiResult<Boolean>

    /**
     * 获取用户收藏的歌单列表
     *
     * @param limit 一页数据量（最多500条）
     * @param offset 偏移量
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/subed/get/v2")
    suspend fun loadMySubedPlaylist(
        @Field("limit") limit: Int = 500,
        @Field("offset") offset: Int = 0
    ): ApiResult<MultiPlaylistsDTO>

    /**
     * 获取用户创建歌单列表（含红心歌单）
     *
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/playlist/created/get/v2")
    suspend fun loadMyCreatePlaylist(
        @Field("limit") limit: Int = 500,
        @Field("offset") offset: Int = 0
    ): ApiResult<MultiPlaylistsDTO>

    /**
     * 获取雷达歌单
     * https://developer.music.163.com/st/developer/document?docId=7be7ab491ae04c299584d88ad1ba4e55
     */
    @POST("/openapi/music/basic/playlist/radar/get")
    suspend fun fetchRadarPlaylist(): ApiResult<List<PlaylistDTO>>
}