package com.smart.neteasemusic.sdk.data.artist

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.ArtistResultDTO
import com.smart.neteasemusic.sdk.data.bean.ArtistZoneDTO
import com.smart.neteasemusic.sdk.data.bean.MusicInfoDTO
import com.smart.neteasemusic.sdk.data.bean.SingerAlbumDTO

/**
 * @author david.long 2026/4/27 18:57
 */
class ArtistApiImpl : BaseApiImpl() {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(ArtistApi::class.java)
    }

    /**
     * 根据tab查询歌手列表
     */
    suspend fun fetchArtistList(
        areaTab: String = "all", bandTab: String = "all", limit: Int = 10, offset: Int = 0
    ): State<ArtistResultDTO> {
        return startFetch {
            api.fetchArtistList(areaTab, bandTab, limit, offset)
        }
    }

    /**
     * 获取歌手的热门歌曲
     */
    suspend fun getArtistSong(
        artistId: String, offset: Int, type: Int = 1, limit: Int = 20, qualityFlag: Boolean = true
    ): State<List<MusicInfoDTO>> {
        return startFetch { api.getArtistHotSong(artistId, type, limit, offset, qualityFlag) }
    }

    /**
     * 获取歌手的专辑
     */
    suspend fun getArtistAlbum(
        artistId: String, offset: Int, sortType: String = "pubTime", limit: Int = 20,
    ): State<SingerAlbumDTO> {
        return startFetch { api.getArtistAlbum(artistId, sortType, limit, offset) }
    }

    /**
     * 查询歌手分类
     */
    suspend fun fetchArtistZone(): State<ArtistZoneDTO> {
        return startFetch { api.fetchArtistZone() }
    }
}