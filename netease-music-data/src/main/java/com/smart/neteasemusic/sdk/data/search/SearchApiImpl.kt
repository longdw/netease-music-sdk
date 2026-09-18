package com.smart.neteasemusic.sdk.data.search

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.MultiPlaylistsDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.data.bean.PageAlbumDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastListDTO
import com.smart.neteasemusic.sdk.data.bean.SearchAlbumsResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchArtistsResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchHotRecordDTO
import com.smart.neteasemusic.sdk.data.bean.SearchMusicResultDTO
import com.smart.neteasemusic.sdk.data.bean.SearchSuggestResultDTO
import com.smart.neteasemusic.sdk.data.bean.VoiceSearchDataDTO

/**
 * @author keli.sang 2026/1/23 13:47
 */
class SearchApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(SearchApi::class.java)
    }

    /**
     * 根据关键字搜索艺人
     *
     * @param keyword
     * @param limit
     * @return
     */
    suspend fun searchArtistsByKeyword(
        keyword: String,
        limit: Int = 100,
    ): State<SearchArtistsResultDTO> {
        return startFetch {
            api.searchArtistsByKeyword(keyword, limit)
        }
    }

    /**
     * 查询艺人下的热门歌曲
     *
     * [artistsId] 艺人Id
     * [limit] 前N首歌（最多获取前500首）
     */
    suspend fun queryArtistTopSongList(
        artistsId: String,
        limit: Int = 100,
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = api.queryArtistTopSongList(artistsId, limit)
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }

    /**
     * 获取艺人下的专辑列表
     *
     * [artistId] 艺人Id
     * [limit] 每页数据
     * [offset] 页码，从0开始
     */
    suspend fun queryArtistAlbumList(
        artistId: String,
        limit: Int = 20,
        offset: Int = 0,
    ): State<PageAlbumDTO> {
        return startFetch {
            api.queryArtistAlbumList(artistId, limit = limit, offset = offset)
        }
    }

    /**
     * 根据关键字搜索歌曲
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    suspend fun searchSongByKeyword(
        keyword: String,
        limit: Int = 100,
        offset: Int = 0,
    ): State<SearchMusicResultDTO> {
        return startFetch {
            api.searchSongByKeyword(keyword, limit = limit, offset = offset)
        }
    }

    /**
     * 搜索播客
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    suspend fun searchVoices(
        keyword: String, limit: Int = 10,
        offset: Int = 0,
    ): State<PodcastListDTO> {
        return startFetch {
            api.searchVoices(keyword, limit = limit, offset = offset)
        }
    }

    /**
     * 根据关键字搜索歌单
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    suspend fun searchPlayListByKeyword(
        keyword: String,
        limit: Int = 100,
        offset: Int = 0,
    ): State<MultiPlaylistsDTO> {
        return startFetch {
            api.searchPlayListByKeyword(keyword, limit = limit, offset = offset)
        }
    }

    /**
     * 根据关键字搜索专辑
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    suspend fun searchAlbumsByKeyword(
        keyword: String,
        limit: Int = 100,
        offset: Int = 0,
    ): State<SearchAlbumsResultDTO> {
        return startFetch {
            api.searchAlbumsByKeyword(keyword, limit = limit, offset = offset)
        }
    }

    /**
     * 获取搜索提示词
     *
     * @param keyword
     */
    suspend fun queryHotSearchSuggest(keyword: String): State<SearchSuggestResultDTO> {
        return startFetch {
            api.queryHotSearchSuggest(keyword)
        }
    }

    /**
     * 获取搜索热词
     *
     * @param limit
     * @return
     */
    suspend fun queryHotSearchWords(limit: Int = 30): State<List<SearchHotRecordDTO>> {
        return startFetch {
            api.queryHotSearchWords(limit = limit)
        }
    }

    /**
     * 语音搜索
     *
     * [mediaName] 资源名称 必选
     * [mediaType] 资源类型 0 开放泛词 1 歌曲 2专辑 3 艺人 5 歌单
     * [artistName] 艺人
     * [albumName] 专辑
     * [sortOrder] 资源按时间排序，比如新歌、新专辑用1
     */
    suspend fun voiceSearch(
        mediaName: String,
        mediaType: String,
        artistName: String,
        albumName: String
    ): State<VoiceSearchDataDTO> {
        return startFetch {
            api.voiceSearch(
                mediaName,
                mediaType,
                artistName,
                albumName,
                sortOrder = "1"
            )
        }
    }
}