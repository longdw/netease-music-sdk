package com.smart.neteasemusic.sdk.data.lyric

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.LyricInfoDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/2/27 13:42
 */
fun interface LyricApi {

    @FormUrlEncoded
    @POST("/openapi/music/basic/song/lyric/get/v2")
    suspend fun fetchLyric(@Field("songId") songId: String): ApiResult<LyricInfoDTO>
}