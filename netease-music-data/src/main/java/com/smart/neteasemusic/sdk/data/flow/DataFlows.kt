package com.smart.neteasemusic.sdk.data.flow

import com.smart.neteasemusic.sdk.data.OpenApiSDK
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author david.long 2026/2/11 17:11
 */
/**
 * 收藏或取消收藏会通过该StateFlow通知
 * Pair(isCollect, SongInfo)
 * isCollect - true 收藏 false取消收藏
 */
val collectMusicStateFlow = OpenApiSDK.getSongApi().collectMusicStateFlow.asStateFlow()

/**
 * 收藏或取消收藏歌单会通过该StateFlow通知
 * Pair(isCollect, playlistId)
 * isCollect - true 收藏 false取消收藏
 */
val collectPlaylistStateFlow = OpenApiSDK.getPlaylistApi().collectPlaylistStateFlow.asStateFlow()
val collectPodcastListStateFlow = OpenApiSDK.getPodcastApi().collectPodcastListStateFlow.asStateFlow()