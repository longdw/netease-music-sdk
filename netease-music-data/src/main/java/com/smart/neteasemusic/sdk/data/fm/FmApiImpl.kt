package com.smart.neteasemusic.sdk.data.fm

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.MusicInfo

/**
 * @author keli.sang 2026/2/27 9:45
 */
class FmApiImpl : BaseApiImpl() {
    private val api by lazy {
        NeRetrofit.getRetrofit().create(FmApi::class.java)
    }

    suspend fun loadPrivateFmPlaylist(limit: Int = 500): State<List<MusicInfo>> {
        return startFetch {
            val result = api.loadPrivateFmPlaylist(limit)
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }
}