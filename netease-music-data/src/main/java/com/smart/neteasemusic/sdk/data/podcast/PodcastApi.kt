package com.smart.neteasemusic.sdk.data.podcast

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.oauth.OAuthConst
import com.smart.neteasemusic.sdk.data.bean.FallsVoiceListDTO
import com.smart.neteasemusic.sdk.data.bean.MyCollectPodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastCategoryTagDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastListDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastUrlDTO
import com.smart.neteasemusic.sdk.data.bean.SceneDTO
import com.smart.neteasemusic.sdk.data.bean.SceneFmVoiceDTO
import com.smart.neteasemusic.sdk.data.bean.VoiceDTO
import com.smart.neteasemusic.sdk.data.bean.WeekNewsPodcastDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/2/4 15:39
 */
interface PodcastApi {

    /**
     * 获取声音播放地址（新版）
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/play/url/get/v1")
    suspend fun getPodcastUrlV1(
        @Field("resourceId") resourceId: String,
        @Field("resourceType") resourceType: String,
    ): ApiResult<PodcastUrlDTO>

    /**
     * 我收藏的播客(瀑布流获取)
     *
     * @param cursor 游标（首次null,下一次的cursor是上次请求结果中返回的cursor）
     * @param limit 每页展示条数，默认100条
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/fallspage/my/collect")
    suspend fun getMyCollectPodcast(
        @Field("cursor") cursor: String? = "",
        @Field("limit") limit: Int? = 100,
    ): ApiResult<MyCollectPodcastDTO>

    /**
     * 获取推荐场景列表
     *
     * @param channelId 业务所属渠道id
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/scene/fm/list")
    suspend fun getRecommendFmScenes(
        @Field("channelId") channelId: String = OAuthConst.appId,
    ): ApiResult<List<SceneDTO>>

    /**
     * 获取推荐声音资源
     *
     * @param channelId 业务所属渠道id
     * @param sceneId 场景id
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/scene/fm/voice/list")
    suspend fun getRecommendVoiceFromScene(
        @Field("channelId") channelId: String = OAuthConst.appId,
        @Field("sceneId") sceneId: String,
    ): ApiResult<List<SceneFmVoiceDTO>>


    /**
     * 获取播客的一级分类
     */
    @POST("/openapi/voice/category/top")
    suspend fun getCategoryPodcastTop(): ApiResult<List<PodcastCategoryTagDTO>>


    /**
     * 获取播客的二级分类
     * @param parentId 一级分类id
     */
    @FormUrlEncoded
    @POST("/openapi/voice/category/second")
    suspend fun getCategoryPodcastSecond(
        @Field("parentId") parentId: Long
    ): ApiResult<List<PodcastCategoryTagDTO>>

    /**
     * 获取分类播单
     * @param limit 一页数据量
     * @param offset 分页偏移量
     * @param categoryType 1:一级分类；2：二级分类
     * @param categoryId 分类id
     */
    @FormUrlEncoded
    @POST("/openapi/voice/category/voice")
    suspend fun getCategoryPodcast(
        @Field("limit") limit: Int,
        @Field("offset") offset: Int,
        @Field("categoryType") categoryType: Int,
        @Field("categoryId") categoryId: Long,
    ): ApiResult<PodcastListDTO>


    /**
     * 获取播单下的声音列表
     * @param radioId 播单id
     * @param page 分页参数
     * @param pageDown 是否翻页
     * @param asc 是否升序
     * @param targetProgramId 目标节目id
     * @param isRangeTarget 是否范围目标
     * @param orderlySnapShotProgramIds 顺序快照节目id
     */
    @FormUrlEncoded
    @POST("/openapi/voice/fall/list")
    suspend fun getVoicesInPodcast(
        @Field("radioId") radioId: String,
        @Field("page") page: String = "{\"size\":25,\"cursor\":\"\"}",
        @Field("pageDown") pageDown: Boolean?,
        @Field("asc") asc: Boolean?,
        @Field("targetProgramId") targetProgramId: Long?,
        @Field("isRangeTarget") isRangeTarget: Boolean?,
        @Field("orderlySnapShotProgramIds") orderlySnapShotProgramIds: String?,
    ): ApiResult<FallsVoiceListDTO>



    /**
     * 猜你喜欢（推荐播单）
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/recommend/guess/like/radio/list")
    suspend fun getYouLikePodcast(
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): ApiResult<List<PodcastDTO>>

    /**
     * 获取本周上新播客
     */
    @FormUrlEncoded
    @POST("/openapi/podcast/block/module/content")
    suspend fun getWeekNewsPodcast(@Field("refresh")refresh: Boolean = true): ApiResult<WeekNewsPodcastDTO>
    /**
     * 批量获取声音详情信息
     *
     * @param programIds 声音资源id,多个id逗号分割
     * @param needPlayRecord 是否需要播放记录
     */
    @FormUrlEncoded
    @POST("/openapi/dj/program/info/list")
    suspend fun getProgramList(
        @Field("programIds") programIds: String,
        @Field("needPlayRecord") needPlayRecord: Boolean = false,
    ): ApiResult<List<VoiceDTO>>

    @FormUrlEncoded
    @POST("/openapi/podcast/subscribe")
    suspend fun subscribePodcast(@Field("radioId") radioId: String): ApiResult<Boolean>

    @FormUrlEncoded
    @POST("/openapi/podcast/unsubscribe")
    suspend fun unSubscribePodcast(@Field("radioId") radioId: String): ApiResult<Boolean>
}