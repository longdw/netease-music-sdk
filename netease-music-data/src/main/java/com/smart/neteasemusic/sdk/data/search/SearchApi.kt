package com.smart.neteasemusic.sdk.data.search

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.MultiPlaylistsDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.PageAlbumDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastListDTO
import com.smart.neteasemusic.sdk.data.bean.SearchAlbumsResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchArtistsResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchHotRecordDTO
import com.smart.neteasemusic.sdk.data.bean.SearchMusicResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchSuggestResultDTO
import com.smart.neteasemusic.sdk.data.bean.VoiceSearchDataDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author keli.sang 2026/1/23 13:45
 */
interface SearchApi {

    @Keep
    companion object {
        /** 排序依据-发行时间 */
        const val SORT_TYPE_PUB_TIME = "pubTime"

        /** 排序依据-热度 */
        const val SORT_TYPE_SCORE = "score"
    }

    /**
     * 根据关键字搜索专辑
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/album/get/v2")
    suspend fun searchAlbumsByKeyword(
        @Field("keyword") keyword: String,
        @Field("limit") limit: Int = 100,
        @Field("offset") offset: Int = 0,
    ): ApiResult<SearchAlbumsResultDTO>

    /**
     * 根据关键字搜索歌单
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/playlist/get/v2")
    suspend fun searchPlayListByKeyword(
        @Field("keyword") keyword: String,
        @Field("limit") limit: Int = 100,
        @Field("offset") offset: Int = 0,
    ): ApiResult<MultiPlaylistsDTO>

    /**
     * 根据关键字搜索艺人
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/artists/get/v2")
    suspend fun searchArtistsByKeyword(
        @Field("keyword") keyword: String,
        @Field("limit") limit: Int = 100,
        @Field("offset") offset: Int = 0,
    ): ApiResult<SearchArtistsResultDTO>

    /**
     * 根据关键字搜索歌曲
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/song/get/v2")
    suspend fun searchSongByKeyword(
        @Field("keyword") keyword: String,
        @Field("limit") limit: Int = 100,
        @Field("offset") offset: Int = 0,
    ): ApiResult<SearchMusicResultDTO>

    /**
     * 获取搜索热词
     *
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/hot/keyword/get/v2")
    suspend fun queryHotSearchWords(
        @Field("limit") limit: Int = 30,
    ): ApiResult<List<SearchHotRecordDTO>>

    /**
     * 获取搜索提示词
     *
     * @param keyword
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/search/suggest/keyword/get/v2")
    suspend fun queryHotSearchSuggest(
        @Field("keyword") keyword: String,
    ): ApiResult<SearchSuggestResultDTO>

    /**
     * 搜索播客
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @FormUrlEncoded
    @POST("/openapi/search/multi/terminal/voicelist")
    suspend fun searchVoices(
        @Field("keyword") keyword: String,
        @Field("limit") limit: Int = 10,
        @Field("offset") offset: Int = 0,
    ): ApiResult<PodcastListDTO>

    /**
     * 获取艺人下的专辑列表
     *
     * [artistId] 艺人Id
     * [sortType] 排序依据:"pubTime":发行时间,"score":热度
     * [limit] 每页数据
     * [offset] 页码，从0开始
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/artists/album/list/bytype/get/v3")
    suspend fun queryArtistAlbumList(
        @Field("artistId") artistId: String,
        @Field("sortType") sortType: String = SORT_TYPE_PUB_TIME,
        @Field("limit") limit: Int = 20,
        @Field("offset") offset: Int = 0,
    ): ApiResult<PageAlbumDTO>

    /**
     * 查询艺人下的热门歌曲
     *
     * [artistsId] 艺人Id
     * [top] 前N首歌（最多获取前500首）
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/artists/hotsong/list/get/v2")
    suspend fun queryArtistTopSongList(
        @Field("artistsId") artistsId: String,
        @Field("top") top: Int = 500,
    ): ApiResult<List<MusicInfoDTO>>

    /**
     * 语音搜索
     *
     * [mediaName] 资源名称 必选
     * [mediaType] 资源类型 0 开放泛词 1 歌曲 2专辑 3 艺人 5 歌单
     * [artistName] 艺人
     * [albumName] 专辑
     * [sortOrder] 资源按时间排序，比如新歌、新专辑用1
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/voice/assistant/multi/search")
    suspend fun voiceSearch(
        @Field("mediaName") mediaName: String,
        @Field("mediaType") mediaType: String,
        @Field("artistName") artistName: String,
        @Field("albumName") albumName: String,
        @Field("sortOrder") sortOrder: String,
    ): ApiResult<VoiceSearchDataDTO>

}