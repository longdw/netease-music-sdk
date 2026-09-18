package com.smart.neteasemusic.sdk.data.purchased

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.PurchasedAlbumDTO
import com.smart.neteasemusic.sdk.data.bean.PurchasedPodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PurchasedSongDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author chenLin 2026/5/19 14:15
 */
interface PurchasedApi {
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/paid/get")
    suspend fun getPurchasedMusic(
        @Field("offset") offset: Int,
        @Field("limit") limit: Int,
        @Field("qualityFlag") qualityFlag: Boolean
    ): ApiResult<PurchasedSongDTO>

    @FormUrlEncoded
    @POST("/openapi/music/basic/album/paid/get/v2")
    suspend fun getPurchasedAlbum(
        @Field("offset") offset: Int,
        @Field("limit") limit: Int
    ): ApiResult<PurchasedAlbumDTO>

    @FormUrlEncoded
    @POST("/openapi/voicelist/bought/list")
    suspend fun getPurchasedPodcast(
        @Field("offset") offset: Int,
        @Field("limit") limit: Int
    ): ApiResult<PurchasedPodcastDTO>
}