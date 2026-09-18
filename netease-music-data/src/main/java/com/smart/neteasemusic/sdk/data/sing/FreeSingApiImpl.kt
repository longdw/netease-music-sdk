package com.smart.neteasemusic.sdk.data.sing

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.toJson
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.AudioDTO

/**
 * @author chenLin 2026/3/19 10:38
 */
class FreeSingApiImpl : BaseApiImpl() {
    private val api by lazy {
        NeRetrofit.getRetrofit().create(FreeSingApi::class.java)
    }

    /**
     * 通过歌曲id判断是否支持随心唱
     */
    suspend fun isSupportKSong(id: String): Boolean {
        val ids = listOf(id).toJson()
        val result = startFetch {
            api.getSongFreelySing(ids)
        }
        return if (result is SUCCESS) {
            val hasMultiTrack = result.data[0].hasMultiTrack?:false
            SDKLogUtil.i("FreeSingApiImpl isSupportKSong = $hasMultiTrack")
            hasMultiTrack
        } else {
            false
        }
    }

    /**
     * 通过歌曲id获取随心唱的详情信息
     */
    suspend fun getSongFreelySingDetail(songId: String): AudioDTO? {
        val result = startFetch {
            api.getSongFreelySingDetail(listOf(songId).toJson())
        }
        return if (result is SUCCESS) {
            result.data[0].multiTrack
        } else {
            SDKLogUtil.e("FreeSingApiImpl getSongFreelySingDetail error")
            null
        }
    }
}