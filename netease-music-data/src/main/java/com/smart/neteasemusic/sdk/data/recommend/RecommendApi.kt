package com.smart.neteasemusic.sdk.data.recommend

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.BannersDTO
import com.smart.neteasemusic.sdk.data.bean.MusicCriticismDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoMixDTO
import com.smart.neteasemusic.sdk.data.bean.OpenApiZoneGroupDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistMixResultDTO
import com.smart.neteasemusic.sdk.data.bean.RecommendCoverDTO
import com.smart.neteasemusic.sdk.data.bean.RecommendPlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.SceneMusicTagDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/1/14 09:13
 */
interface RecommendApi {

    @Keep
    companion object {
        //热歌ID
        const val hotMusicPlaylistId = "3766C193B335B28911394F1EED9453C4"
        const val sunoAiPageCode = "a55556c6bc9b4ea2ba055c7d0befe05e"

        const val sunoPlaylistType = "PLAYLIST"
    }


    /**
     * 获取榜单列表 --排行榜
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/toplist/get/v2")
    suspend fun fetchTopList(
        @Field("limit") limit: Int = 500,
        @Field("offset") offset: Int = 0,
    ): ApiResult<List<PlaylistDTO>>

    /**
     * 获取推荐歌单列表
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/recommend/playlist/get")
    suspend fun fetchRecommendPlaylist(
        @Field("limit") limit: Int = 30,
        @Field("offset") offset: Int = 0,
    ): ApiResult<RecommendPlaylistDTO>

    /**
     * 获取日推mix
     */
    @POST("/openapi/music/basic/recommend/daily/mix")
    suspend fun fetchDailyRecommendMix(): ApiResult<PlaylistMixResultDTO>

    /**
     * 获取Suno Ai内容
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/yida/page/get")
    suspend fun fetchSunoAiList(@Field("pageCode") pageCode: String): ApiResult<OpenApiZoneGroupDTO>


    /**
     * 获取场景音乐歌曲列表
     * withPlayUrl 是否返回播放URL
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/radio/tag/fm/get/v2")
    suspend fun fetchSceneMusicList(
        @Field("tag") tag: String,
        @Field("limit") limit: Int = 10,
        @Field("withPlayUrl") withPlayUrl: Boolean = true,
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取banner
     */
    @POST("/openapi/music/banner/get")
    suspend fun fetchBannerList(): ApiResult<BannersDTO>

    /**
     * 每日推荐歌曲列表
     *
     * @param limit 一页数据量（最多500条）
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/recommend/songlist/get/v2")
    suspend fun fetchDailyRecommendSong(
        @Field("limit") limit: Int = 500,
        @Field("qualityFlag") qualityFlag: Boolean = false,
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取每日推荐封面
     */
    @POST("/openapi/music/basic/recommend/daily/image")
    suspend fun fetchDailyRecommendCover(): ApiResult<RecommendCoverDTO>

    /**
     * 获取乐评
     */
    @POST("/openapi/music/heart/beat/widget/get")
    @FormUrlEncoded
    suspend fun loadMusicCriticism(
        @Field("limit") limit: Int = 10,
        @Field("contentType") contentType: String = "CALENDAR",
    ): ApiResult<List<MusicCriticismDTO>>

    /**
     * 获取场景音乐标签
     * https://developer.music.163.com/st/developer/document?docId=8e151e88295e46c195c4786ad382cc5f
     */
    @POST("/openapi/music/basic/scene/radio/tags/get")
    suspend fun fetchSceneMusicTagList(): ApiResult<List<SceneMusicTagDTO>>

    /**
     * 获取场景音乐标签下的歌曲
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/scene/radio/get")
    suspend fun fetchSceneMusicList(
        @Field("scene") scene: String,
        @Field("limit") limit: Int = 10,
        @Field("withPlayUrl") withPlayUrl: Boolean = false,
        @Field("qualityFlag") qualityFlag: Boolean = false
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取相似歌曲-歌曲列表（日推mix）
     * https://developer.music.163.com/st/developer/document?docId=707058df01fe40288a82ea3581e9e935
     * @param sourceIds 歌曲id，/openapi/music/basic/recommend/daily/mix返回的song_fm中歌曲id
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/private/fm/roaming/song/list")
    suspend fun fetchSimilarMusicList(
        @Field("type") type: String = "mode",
        @Field("code") code: String = "SIMILAR",
        @Field("sourceType") sourceType: String = "song",
        @Field("sourceIds") sourceIds: String
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取风格日推-歌曲列表（日推mix）
     * 固定30首
     * @param categoryId 分类id ，/openapi/music/basic/recommend/daily/mix返回的tag_daily_rec中categoryId
     * @param tagId 标签id /openapi/music/basic/recommend/daily/mix返回的tag_daily_rec中tagId
     * @param songId 歌曲id，/openapi/music/basic/recommend/daily/mix返回的tag_daily_rec中resourceId
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/daily/style/get")
    suspend fun fetchDailyStyleMusicList(
        @Field("categoryId") categoryId: String?,
        @Field("tagId") tagId: String?,
        @Field("songId") songId: String?
    ): ApiResult<MusicInfoMixDTO>

    /**
     * 心动模式（日推mix）
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/song/play/intelligence/get")
    suspend fun fetchIntelligenceMusicList(
        @Field("playlistId") playlistId: String?,
        @Field("songId") songId: String?,
        @Field("type") type: String = "fromPlayAll",
        @Field("count") count: Int = 150
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 获取私人漫游场景歌曲
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/private/fm/roaming/song/list")
    suspend fun fetchRoamingMusicList(
        @Field("type") type: String = "mode",
        @Field("code") code: String = "DEFAULT",
        @Field("limit") limit: Int = 3
    ): ApiResult<List<MusicInfoDTO>>

}