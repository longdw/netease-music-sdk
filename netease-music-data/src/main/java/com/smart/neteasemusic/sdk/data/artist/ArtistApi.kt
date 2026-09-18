package com.smart.neteasemusic.sdk.data.artist

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.ArtistResultDTO
import com.smart.neteasemusic.sdk.data.bean.ArtistZoneDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.SingerAlbumDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 艺人（歌手）相关
 * @author david.long 2026/4/27 18:52
 */
interface ArtistApi {

    /**
     * 查询歌手分类
     */
    @POST("/openapi/music/basic/artist/zone/tab/list")
    suspend fun fetchArtistZone(): ApiResult<ArtistZoneDTO>

    /**
     * 根据tab查询歌手信息（新版）
     * @param areaTab 区域，如：全部、华语、欧美
     * @param bandTab 如：全部、男、女
     */
    @POST("/openapi/music/basic/artist/zone/get/by/tab")
    @FormUrlEncoded
    suspend fun fetchArtistList(
        @Field("areaTab") areaTab: String,
        @Field("bandTab") bandTab: String,
        @Field("limit") limit: Int = 10,
        @Field("offset") offset: Int = 0
    ): ApiResult<ArtistResultDTO>


    /**
     * 获取歌手的热门歌曲
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/artists/song/list/get")
    suspend fun getArtistHotSong(
        @Field("artistId") artistId: String,
        @Field("type")type: Int,
        @Field("limit")limit: Int,
        @Field("offset")offset: Int,
        @Field("qualityFlag")qualityFlag: Boolean
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取歌手的专辑
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/artists/album/list/bytype/get/v3")
    suspend fun getArtistAlbum(
        @Field("artistId") artistId: String,
        @Field("sortType")sortType: String,
        @Field("limit")limit: Int,
        @Field("offset")offset: Int
    ): ApiResult<SingerAlbumDTO>
}