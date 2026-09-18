package com.smart.neteasemusic.sdk.data.album

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.AlbumDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.PageAlbumMusicDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author ezio.ying 2026/3/20 16:39
 */
interface AlbumAPI {
    /**
     * 获取专辑详情
     * @param descFlag 是否需要专辑介绍
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/album/detail/get/v2")
    suspend fun fetchAlbumDetail(
        @Field("albumId") albumId: String,
        @Field("descFlag") descFlag : Boolean = false
    ): ApiResult<AlbumDTO>

    /**
     * 获取专辑下的歌曲列表v2是不带分页的
     * @param qualityFlag 是否下发音质
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/album/song/list/get/v2")
    suspend fun fetchAlbumSongListV2(
        @Field("albumId") albumId: String,
        @Field("qualityFlag") qualityFlag : Boolean = false
    ): ApiResult<List<MusicInfoDTO>>

    /**
     *V3带分页 获取专辑下的歌曲列表
     */

  @FormUrlEncoded
    @POST("/openapi/music/basic/album/song/list/get/v3")
    suspend fun fetchAlbumSongList(
        @Field("albumId") albumId: String,
        @Field("limit") limit : Int = 10,
        @Field("offset") offset : Int = 0,
        @Field("qualityFlag") qualityFlag : Boolean = false
    ): ApiResult<PageAlbumMusicDTO>

}
