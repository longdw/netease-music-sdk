package com.smart.neteasemusic.sdk.data.hifi

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.HiFiZoneHomePageResDTO
import com.smart.neteasemusic.sdk.data.bean.HiFiZoneModulePageResDTO
import com.smart.neteasemusic.sdk.data.bean.HiresDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author chenLin 2026/3/18 13:54
 */
interface HifiApi {

    /**
     * 获取hires内容
     *
     * @param resourceTypes - song,playlist,album
     * @param withUrl - 是否需要播放url（默认为true）
     * @param bitrate - 播放URL的码率 固定传1999（默认320）
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/hi/res/get")
    suspend fun fetchHiresData(
        @Field("resourceTypes") resourceTypes:String,
        @Field("withUrl") withUrl: Boolean?,
        @Field("bitrate") bitrate: Int?
    ): ApiResult<HiresDTO>

    /**
     * 获取HiFi专区
     *
     * @param code 页面code
     * @param supportVivid 是否下发 audio vivid 内容
     * @return [ApiResult] 包含推荐场景列表数据
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/yida/hifi/get")
    suspend fun fetchHiFiItems(
        @Field("code") code: String,
        @Field("supportVivid") supportVivid: Boolean = false
    ): ApiResult<HiFiZoneHomePageResDTO>

    /**
     * 获取HiFi专区模块数据
     *
     * @param parentPosCode 父页面code
     * @param posCode 子页面code
     * @param limit 每页展示条数
     * @param offset 偏移量
     * @return [ApiResult] 包含HiFi专区模块数据
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/yida/hifi/module/get")
    suspend fun fetchHiFiModule(
        @Field("parentPosCode") parentPosCode: String,
        @Field("posCode") posCode: String,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): ApiResult<HiFiZoneModulePageResDTO>
}