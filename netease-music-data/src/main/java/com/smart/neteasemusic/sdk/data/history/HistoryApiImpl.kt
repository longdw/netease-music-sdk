package com.smart.neteasemusic.sdk.data.history

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.AlbumDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.data.bean.PlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastDTO

/**
 * 播放历史记录
 * @author david.long 2026/4/22 13:19
 */
class HistoryApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(HistoryApi::class.java)
    }

    /**
     * 获取最近播放歌单列表
     */
    suspend fun fetchPlaylistHistory(limit: Int = 100): State<List<PlaylistDTO>> {
        return startFetch {
            val result = api.fetchPlaylistHistory(limit)
            ApiResult(
                code = result.code,
                data = result.data?.records?.map {
                    it.record
                },
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }

    /**
     * 获取最近播放专辑列表
     */
    suspend fun fetchAlbumHistory(limit: Int = 100): State<List<AlbumDTO>> {
        return startFetch {
            val result = api.fetchAlbumHistory(limit)
            ApiResult(
                code = result.code,
                data = result.data?.records?.map {
                    it.record
                },
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }

    /**
     * 获取最近播放播客列表
     */
    suspend fun fetchPodcastHistory(limit: Int = 100): State<List<PodcastDTO>> {
        return startFetch {
            val result = api.fetchPodcastHistory(limit)
            ApiResult(
                code = result.code,
                data = result.data?.list,
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }

    /**
     * 获取最近播放歌曲列表
     */
    suspend fun fetchMusicHistory(limit: Int = 300, qualityFlag: Boolean = false): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchMusicHistory(limit, qualityFlag)
            ApiResult(
                code = result.code,
                data = result.data?.songListVo?.map {
                    it.toMusicInfo()
                },
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }
}