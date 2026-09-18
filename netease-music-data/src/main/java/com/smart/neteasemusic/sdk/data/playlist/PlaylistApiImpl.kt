package com.smart.neteasemusic.sdk.data.playlist

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.smart.neteasemusic.sdk.data.bean.MultiPlaylistsDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistTagDTO
import com.smart.neteasemusic.sdk.data.bean.PlaylistsFromTagDTO
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author david.long 2026/1/28 18:52
 */
class PlaylistApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(PlaylistApi::class.java)
    }

    internal val collectPlaylistStateFlow = MutableStateFlow<State<Pair<Boolean, String>>?>(null)

    /**
     * 获取歌单标签，如：华语、粤语、欧美、夜晚、学习等
     */
    suspend fun fetchPlaylistTags(): State<List<PlaylistTagDTO>> {
        return startFetch {
            api.fetchPlaylistTags()
        }
    }

    /**
     * 根据标签获取歌单
     * @param tag 标签名称
     * @param limit 请求个数，默认10
     * @param offset 请求偏移，默认0
     */
    suspend fun fetchPlaylistFromTag(
        tag: String,
        limit: Int = 10,
        offset: Int = 0
    ): State<PlaylistsFromTagDTO> {
        return startFetch {
            api.fetchPlaylistFromTag(tag, limit, offset)
        }
    }

    /**
     * 获取歌单的详细信息
     * @param playlistId 歌单id
     */
    suspend fun fetchPlaylistDetail(
        playlistId: String
    ): State<PlaylistDTO> {
        return startFetch {
            api.fetchPlaylistDetail(playlistId)
        }
    }

    /**
     * 获取歌单里的歌曲列表
     */
    suspend fun fetchPlaylistSong(
        playlistId: String,
        limit: Int = 500,
        offset: Int = 0,
        qualityFlag: Boolean = false
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchPlaylistSong(playlistId, limit, offset, qualityFlag)
            ApiResult(
                code = result.code,
                data = result.data?.map {
                    it.toMusicInfo()
                },
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }

    /**
     * 用户收藏歌单
     */
    suspend fun collectPlaylist(
        playlistId: String
    ):State<Boolean> {
        val state = runCatching {
            api.subscribePlaylist(playlistId)
        }.fold(
            onSuccess = { result ->
                if (result.isSuccess()) {
                    collectPlaylistStateFlow.value = State.success(Pair(true, playlistId))
                    success(true)
                } else {
                    error(IllegalArgumentException("subscribe playlist failed"))
                }
            },
            onFailure = {
                error(it)
            }
        )
        return state
    }

    /**
     * 用户取消收藏歌单
     */
    suspend fun unsubscribePlaylist(
        playlistId: String
    ):State<Boolean> {
        val state = runCatching {
            api.unsubscribePlaylist(playlistId)
        }.fold(
            onSuccess = { result ->
                if (result.isSuccess()) {
                    collectPlaylistStateFlow.value = State.success(Pair(false, playlistId))
                    success(true)
                } else {
                    error(IllegalArgumentException("subscribe playlist failed"))
                }
            },
            onFailure = {
                error(it)
            }
        )
        return state
    }

    /**
     * 获取我喜欢的歌单
     */
    suspend fun fetchMyLikePlaylist(): State<PlaylistDTO> {
        return startFetch {
            api.fetchMyLikePlaylist()
        }
    }

    /**
     * 获取我喜欢的歌单id
     */
    suspend fun fetchMyLikePlaylistId(): String? {
        return UserManager.getMyLikePlaylistId() ?: run {
            val state = startFetch { api.fetchMyLikePlaylist() }
            return if (state is SUCCESS) {
                val id = state.data.id
                if (id != null) {
                    UserManager.saveMyLikePlaylistId(id)
                }
                id
            } else {
                null
            }
        }

    }

    /**
     * 获取用户创建歌单列表
     */
    suspend fun loadMyCreatePlaylist(
        limit: Int = 500,
        offset: Int
    ): State<MultiPlaylistsDTO> {
        return startFetch {
            api.loadMyCreatePlaylist(limit, offset)
        }
    }

    /**
     * 获取用户收藏的歌单列表
     */
    suspend fun loadMySubedPlaylist(): State<MultiPlaylistsDTO> {
        return startFetch {
            api.loadMySubedPlaylist()
        }
    }

    /**
     * 获取雷达推荐歌单
     */
    suspend fun fetchRadarPlaylist(): State<List<PlaylistDTO>> {
        return startFetch {
            api.fetchRadarPlaylist()
        }
    }

}