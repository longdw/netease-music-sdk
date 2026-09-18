package com.smart.neteasemusic.business.preload

import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.business.ext.thumbnail
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.OpenApiSDK
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import kotlin.collections.forEach

/**
 * 我喜欢歌单预加载策略
 * @author david.long 2026/4/10 11:02
 */
class MyLikePreLoadStrategy : BasePreLoadStrategy() {

    override val playSource: String = PlaySource.MY_LIKE

    override val keyOffset: String = "key_offset_my_like"

    override val keyHasMore: String = "key_has_more_my_like"

    override suspend fun fetchNextPage(offset: Int): State<List<MusicInfo>>? {
        return OpenApiSDK.getPlaylistApi().fetchMyLikePlaylistId()?.let { playlistId ->
            OpenApiSDK.getPlaylistApi().fetchPlaylistSong(playlistId, limit = DEFAULT_LIMIT, offset = offset)
        }
    }

    override fun getPageLimit(): Int {
        return 300
    }

    override suspend fun fetchMusicList(params: Map<String, Any>): State<List<MusicInfo>>? {
        val offset = params["offset"] as? Int ?: 0
        val limit = params["limit"] as? Int ?: getPageLimit()
        return OpenApiSDK.getPlaylistApi().fetchMyLikePlaylistId()?.let { playlistId ->
            OpenApiSDK.getPlaylistApi()
                .fetchPlaylistSong(playlistId, limit = limit, offset = offset)
                .also {
                    SDKLogUtil.i("[MyLikePreLoadStrategy][fetchMusicList] params=$params, it=$it")
                    if (it is SUCCESS) {
                        it.data.forEach { musicInfo ->
                            musicInfo.coverUrl = musicInfo.coverUrl.thumbnail()
                        }
                        tempOffset = offset + it.data.size
                        tempHasMore = it.data.size == limit
                        SDKLogUtil.i("[MyLikePreLoadStrategy][fetchMusicList] this=${this@MyLikePreLoadStrategy}, tempOffset=$tempOffset, tempHasMore=$tempHasMore")
                    }
                }
        }
    }
}