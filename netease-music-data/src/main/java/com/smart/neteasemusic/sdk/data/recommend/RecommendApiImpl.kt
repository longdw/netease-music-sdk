package com.smart.neteasemusic.sdk.data.recommend

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.BannersDTO
import com.smart.neteasemusic.sdk.data.bean.MusicCriticismDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.data.bean.OpenApiZoneGroupDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistMixResultDTO
import com.smart.neteasemusic.sdk.data.bean.RecommendCoverDTO
import com.smart.neteasemusic.sdk.data.bean.RecommendPlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.SceneMusicTagDTO
import com.smart.neteasemusic.sdk.data.playlist.PlaylistApi
import com.smart.neteasemusic.sdk.data.recommend.RecommendApi.Companion.sunoAiPageCode

/**
 * 推荐API
 * @author david.long 2026/1/14 09:07
 */
class RecommendApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(RecommendApi::class.java)
    }

    suspend fun fetchDailyRecommendSong(
        limit: Int = 30,
        qualityFlag: Boolean = false,
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchDailyRecommendSong(limit, qualityFlag)
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }

    suspend fun fetchDailyRecommendCover(): State<RecommendCoverDTO> {
        return startFetch {
            api.fetchDailyRecommendCover()
        }
    }

    suspend fun fetchBannerList(): State<BannersDTO> {
        return startFetch {
            api.fetchBannerList()
        }
    }

    suspend fun fetchTopList(
        limit: Int = 500,
        offset: Int = 0,
    ): State<List<PlaylistDTO>> {
        return startFetch {
            api.fetchTopList(limit, offset)
        }
    }

    /**
     * 获取推荐歌单列表
     */
    suspend fun fetchRecommendPlaylist(
        limit: Int = 30,
        offset: Int = 0,
    ): State<RecommendPlaylistDTO> {
        return startFetch {
            api.fetchRecommendPlaylist(limit, offset)
        }
    }

    /**
     * 获取日推mix
     */
    suspend fun fetchDailyRecommendMix(): State<PlaylistMixResultDTO> {
        return startFetch {
            api.fetchDailyRecommendMix()
        }
    }

    suspend fun fetchSunoAiList(pageCode: String = sunoAiPageCode): State<OpenApiZoneGroupDTO> {
        return startFetch {
            api.fetchSunoAiList(pageCode)
        }
    }

    /**
     * 获取场景音乐标签
     */
    suspend fun fetchSceneMusicTagList(): State<List<SceneMusicTagDTO>> {
        return startFetch {
            api.fetchSceneMusicTagList()
        }
    }

    /**
     * 获取场景音乐标签下的歌曲
     * @param limit 建议3，最多10个
     */
    suspend fun fetchSceneMusicList(
        scene: String,
        limit: Int = 10,
        withPlayUrl: Boolean = false,
        qualityFlag: Boolean = false
    ): State<List<MusicInfo>> {
        require(limit <= 10)
        return startFetch {
            val result = api.fetchSceneMusicList(scene, limit, withPlayUrl, qualityFlag)
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }

    suspend fun loadMusicCriticism(
        limit: Int = 10,
        contentType: String = "CALENDAR",
    ): State<List<MusicCriticismDTO>> {
        return startFetch {
            val result = api.loadMusicCriticism(limit = limit, contentType = contentType)
            ApiResult(data = result.data)

        }
    }

    /**
     * 获取日推各种类型的歌曲列表
     */
    suspend fun fetchDailyMixMusicList(
        resourceType: String,
        resourceId: String,
        limit: Int = 500,
        params: Map<String, String?> = emptyMap()
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = when(resourceType) {
                "daily_song_rec" -> api.fetchDailyRecommendSong(limit)
                "radar",
                "another_radar",
                "hot_song_board",
                "soar_board",
                "new_song_board",
                "mood",
                "once_hear" -> NeRetrofit.getRetrofit().create(PlaylistApi::class.java).fetchPlaylistSong(resourceId, limit)
                "song_fm", "artist_fm" -> api.fetchSimilarMusicList(sourceIds = resourceId)
                "tag_daily_rec" -> {
                    val result = api.fetchDailyStyleMusicList(params["categoryId"], params["tagId"], resourceId)
                    ApiResult(data = result.data?.songListVo)
                }
                "user_like" -> api.fetchIntelligenceMusicList(params["playlist"], params["songId"])
                else -> ApiResult(data = emptyList())
            }
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }

    /**
     * 私人漫游歌曲列表
     */
    suspend fun fetchRoamingMusicList(): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchRoamingMusicList()
            ApiResult(data = result.data?.map {
                it.toMusicInfo()
            })
        }
    }
}