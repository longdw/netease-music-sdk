package com.smart.neteasemusic.sdk.data.purchased

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.PurchasedAlbumDTO
import com.smart.neteasemusic.sdk.data.bean.PurchasedPodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PurchasedSongDTO

/**
 * @author chenLin 2026/5/19 14:22
 */
class PurchasedApiImpl : BaseApiImpl() {
    private val api by lazy {
        NeRetrofit.getRetrofit().create(PurchasedApi::class.java)
    }

    /**
     * 获取已购音乐
     */
    suspend fun getPurchasedMusic(offset: Int,limit: Int): State<PurchasedSongDTO> {
        return startFetch {
            api.getPurchasedMusic(offset, limit, false)
        }
    }

    /**
     * 获取已购专辑
     */
    suspend fun getPurchasedAlbum(offset: Int,limit: Int): State<PurchasedAlbumDTO> {
        return startFetch {
            api.getPurchasedAlbum(offset, limit)
        }
    }

    suspend fun getPurchasedPodcast(offset: Int,limit: Int): State<PurchasedPodcastDTO> {
        return startFetch { api.getPurchasedPodcast(offset, limit) }
    }
}