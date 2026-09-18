package com.smart.neteasemusic.sdk.player.repository

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.podcast.PodcastApi

/**
 * 仅sdk内部使用
 * @author david.long 2026/2/4 15:48
 */
internal object PodcastDataSource {

    private val api by lazy {
        NeRetrofit.getRetrofit().create(PodcastApi::class.java)
    }

    suspend fun getPodcastUrlV1(
        resourceId: String,
        resourceType: String = ""
    ) = try {
        val result = api.getPodcastUrlV1(resourceId, resourceType)
        SDKLogUtil.i("[PodcastDataSource][getPodcastUrlV1] $result")
        if (result.isSuccess()) {
            result.data
        } else {
            null
        }
    } catch (e: Exception) {
        SDKLogUtil.e("[PodcastDataSource][getPodcastUrlV1] error", e)
        null
    }
}