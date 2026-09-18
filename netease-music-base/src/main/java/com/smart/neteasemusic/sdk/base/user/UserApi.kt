package com.smart.neteasemusic.sdk.base.user

import com.smart.neteasemusic.sdk.base.bean.Profile
import com.smart.neteasemusic.sdk.base.network.ApiResult
import retrofit2.http.POST

/**
 * 跟用户相关的接口
 * @author david.long 2026/1/12 08:54
 */
fun interface UserApi {
    /**
     * 获取用户基本信息
     *
     * @return Profile
     */
    @POST("/openapi/music/basic/user/profile/get/v2")
    suspend fun fetchProfile(): ApiResult<Profile>
}