package com.smart.neteasemusic.sdk.data.sing

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.SupportSingDTO
import com.smart.neteasemusic.sdk.data.bean.VocalDetailDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author chenLin 2026/3/19 10:27
 */
interface FreeSingApi {
    /**
     * 歌曲是否支持干声（人声）
     *
     * @param songIds
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/freely/sing/get")
    suspend fun getSongFreelySing(
        @Field("songIds") songIds: String
    ): ApiResult<List<SupportSingDTO>>
    /**
     * 歌曲获取音频详情
     *
     * @param songIds
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/freely/sing/detail")
    suspend fun getSongFreelySingDetail(
        @Field("songIds") songIds: String
    ): ApiResult<List<VocalDetailDTO>>

}