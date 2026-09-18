package com.smart.neteasemusic.sdk.data.album

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.AlbumDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfo

/**
 * @author ezio.ying 2026/3/23 14:54
 */
class AlbumApiImpl : BaseApiImpl() {
    private val api by lazy { NeRetrofit.getRetrofit().create(AlbumAPI::class.java) }
    /**
     * 获取专辑详情
     * @param descFlag 是否需要专辑介绍
     */
    suspend fun fetchAlbumDetail(
        albumId: String,
        descFlag: Boolean = false
    ): State<AlbumDTO> {
        return startFetch { api.fetchAlbumDetail(albumId, descFlag) }
    }

    /**
     * 获取专辑歌曲列表
     * 这个不用了。用V3
     */
    suspend fun fetchAlbumSongListV2(
        albumId: String,
        qualityFlag: Boolean = false
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchAlbumSongListV2(albumId, qualityFlag)
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
     * 获取专辑歌曲列表
     * 新版用这个，带分页
     */
    suspend fun fetchAlbumSongList(
        albumId: String,
        limit : Int = 500,
        offset : Int = 0,
        qualityFlag: Boolean = false
    ): State<List<MusicInfo>> {
        return startFetch {
            val result = api.fetchAlbumSongList(albumId, limit, offset, qualityFlag)
            ApiResult(
                code = result.code,
                data = result.data?.records?.map{
                    it.toMusicInfo()
                },
                subCode = result.subCode,
                message = result.message,
                exception = result.exception
            )
        }
    }
}