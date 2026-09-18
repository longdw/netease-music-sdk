package com.smart.neteasemusic.sdk.data.fm

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 私人FM接口
 * @author keli.sang 2026/2/27 09:43
 */
@Keep
interface FmApi {
    @FormUrlEncoded
    @POST("/openapi/music/basic/radio/fm/get/v2")
    suspend fun loadPrivateFmPlaylist(
        @Field("limit") limit: Int = 500,
        @Field("withPlayUrl") withPlayUrl: Boolean = true,
    ): ApiResult<List<MusicInfoDTO>>
}