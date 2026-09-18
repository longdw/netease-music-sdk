package com.smart.neteasemusic.sdk.data.hifi

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.toJson
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.HiFiZoneHomePageResDTO
import com.smart.neteasemusic.sdk.data.bean.HiFiZoneModulePageResDTO
import com.smart.neteasemusic.sdk.data.bean.HiresDTO

/**
 * @author chenLin 2026/3/18 14:00
 */
class HiFiApiImpl: BaseApiImpl() {
    private val api by lazy {
        NeRetrofit.getRetrofit().create(HifiApi::class.java)
    }
    private val resourceTypes = mutableListOf("song", "playlist", "album")

    /**
     * 获取hires内容
     */
    suspend fun fetchHiresData(): State<HiresDTO> {
        return startFetch {
            api.fetchHiresData(resourceTypes.toJson(), withUrl = true, bitrate = 1999)
        }
    }

    /**
     * 获取HiFi专区
     * @param code 页面code
     * @param supportVivid 是否下发 audio vivid 内容
     */
    suspend fun fetchHiFiItems(
        code: String,
        supportVivid: Boolean = false
    ): State<HiFiZoneHomePageResDTO> {
        return startFetch { api.fetchHiFiItems(code, supportVivid) }
    }

    /**
     * 获取HiFi专区模块数据
     *
     * @param parentPosCode 父页面code
     * @param posCode 子页面code
     * @param limit 每页展示条数
     * @param offset 偏移量
     */
    suspend fun fetchHiFiModule(
        parentPosCode: String,
        posCode: String,
        limit: Int,
        offset: Int
    ): State<HiFiZoneModulePageResDTO> {
        return startFetch { api.fetchHiFiModule(parentPosCode, posCode, limit, offset) }
    }
}