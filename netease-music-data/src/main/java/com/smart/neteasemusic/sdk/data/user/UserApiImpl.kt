package com.smart.neteasemusic.sdk.data.user

import com.smart.neteasemusic.sdk.base.bean.Profile
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.smart.neteasemusic.sdk.base.user.UserApi
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl

/**
 * @author david.long 2026/1/13 19:17
 */
class UserApiImpl : BaseApiImpl() {

    private val userApi by lazy {
        NeRetrofit.getRetrofit().create(UserApi::class.java)
    }

    suspend fun fetchProfile(): State<Profile> {
        return runCatching {
            val result = userApi.fetchProfile()
            val data = result.data
            if (data != null) {
                UserManager.updateProfile(data)
                success(data)
            } else {
                error(IllegalArgumentException("data is null"))
            }
        }.getOrElse {
            error(it)
        }
    }


}