package com.smart.neteasemusic.sdk.data.podcast

import com.smart.neteasemusic.sdk.base.network.NeRetrofit
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.data.base.BaseApiImpl
import com.smart.neteasemusic.sdk.data.bean.FallsVoiceListDTO
import com.smart.neteasemusic.sdk.data.bean.MyCollectPodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastCategoryTagDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastDTO
import com.smart.neteasemusic.sdk.data.bean.PodcastListDTO
import com.smart.neteasemusic.sdk.data.bean.SceneFmVoiceDTO
import com.smart.neteasemusic.sdk.data.bean.SceneDTO
import com.smart.neteasemusic.sdk.data.bean.VoiceDTO
import com.smart.neteasemusic.sdk.data.bean.WeekNewsPodcastDTO
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author keli.sang 2026/2/26 16:51
 */
class PodcastApiImpl : BaseApiImpl() {
    private val api by lazy {
        NeRetrofit.getRetrofit().create(PodcastApi::class.java)
    }
    internal val collectPodcastListStateFlow = MutableStateFlow<State<Pair<Boolean, String>>?>(null)


    /**
     * 获取用户收藏的播客列表
     */
    suspend fun getMyCollectPodcast(
        cursor: String? = "",
        limit: Int? = 100,
    ): State<MyCollectPodcastDTO> {
        return startFetch {
            api.getMyCollectPodcast(cursor, limit)
        }
    }

    /**
     * 获取推荐场景列表
     *
     */
    suspend fun getRecommendFmScenes(): State<List<SceneDTO>> {
        return startFetch {
            api.getRecommendFmScenes()
        }
    }

    /**
     * 获取推荐声音资源
     *
     * @param sceneId 场景id
     * @return
     */
    suspend fun getRecommendVoiceFromScene(sceneId: String): State<List<SceneFmVoiceDTO>> {
        return startFetch {
            api.getRecommendVoiceFromScene(sceneId = sceneId)
        }
    }

    /**
     * 获取播客的一级分类
     */
    suspend fun getTopLevelPodcastCategory(): State<List<PodcastCategoryTagDTO>>{
        return startFetch { api.getCategoryPodcastTop() }
    }

    /**
     * 获取播客的二级分类
     * @param parentId 一级分类id
     */
    suspend fun getSecondLevelPodcastCategory(
        parentId: Long
    ): State<List<PodcastCategoryTagDTO>>{
        return startFetch { api.getCategoryPodcastSecond(parentId) }
    }

    /**
     * 获取分类播单
     * @param limit 一页数据量
     * @param offset 分页偏移量
     * @param categoryType 1:一级分类；2：二级分类
     * @param categoryId 分类id
     */
    suspend fun getCategoryVoice(
        limit: Int = 10,
        offset: Int = 0,
        categoryType: Int = 1,
        categoryId: Long,
    ): State<PodcastListDTO> {
        return startFetch {
            api.getCategoryPodcast(limit, offset, categoryType, categoryId)
        }
    }

    /**
     * 获取猜你喜欢播客
     */
    suspend fun getYouLikePodcast(limit: Int = 10, offset: Int = 0): State<List<PodcastDTO>> {
        return startFetch {
            api.getYouLikePodcast(limit, offset)
        }
    }


    /**
     * 获取本周上新播客
     *
     */
    suspend fun getWeekNewsPodcast(): State<WeekNewsPodcastDTO> {
        return startFetch {
            api.getWeekNewsPodcast()
        }
    }

    /**
     * 获取播单下的声音列表
     * @param radioId 播单id
     * @param page 分页参数
     */
    suspend fun getVoicesInPodcast(
        radioId: String,
        page: String = "{\"size\":25,\"cursor\":\"\"}",
        pageDown: Boolean? = null,
        asc: Boolean? = null,
        targetProgramId: Long? = null,
        isRangeTarget: Boolean? = null,
        orderlySnapShotProgramIds: List<Long>? = null
    ): State<FallsVoiceListDTO> {
        return startFetch {
            api.getVoicesInPodcast(
                radioId = radioId,
                page = page,
                pageDown = pageDown,
                asc = asc,
                targetProgramId = targetProgramId,
                isRangeTarget = isRangeTarget,
                orderlySnapShotProgramIds = orderlySnapShotProgramIds?.joinToString()
            )
        }
    }
    /**
     * 批量获取声音详情信息
     *
     * @param voiceId 声音资源id,多个id逗号分割
     */
    suspend fun getProgramList(voiceId: String): State<List<VoiceDTO>> {
        return startFetch {
            api.getProgramList(voiceId)
        }
    }

    suspend fun subscribePodcast(podcastListId: String): State<Boolean> {
        val state = runCatching {
            api.subscribePodcast(podcastListId)
        }.fold(
            onSuccess = { result ->
                if (result.isSuccess()) {
                    collectPodcastListStateFlow.value = State.success(Pair(true, podcastListId))
                    success(true)
                } else {
                    error(IllegalArgumentException("subscribe podcastListId failed"))
                }
            },
            onFailure = {
                error(it)
            }
        )
        return state
    }

    suspend fun unSubscribePodcast(podcastListId: String): State<Boolean> {
        val state = runCatching {
            api.unSubscribePodcast(podcastListId)
        }.fold(
            onSuccess = { result ->
                if (result.isSuccess()) {
                    collectPodcastListStateFlow.value = State.success(Pair(false, podcastListId))
                    success(true)
                } else {
                    error(IllegalArgumentException("unSubscribe podcastListId failed"))
                }
            },
            onFailure = {
                error(it)
            }
        )
        return state
    }
}