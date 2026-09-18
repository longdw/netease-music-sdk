package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/23 14:32
 */
@JsonClass(generateAdapter = true)
@Keep
data class PlaylistDTO(
    var musics: List<MusicInfoDTO>?,
    // 歌单Id
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "describe") val describe: String?,
    @field:Json(name = "coverImgUrl") var coverImgUrl: String?,
    @field:Json(name = "creatorNickName") val creatorNickName: String?,
    @field:Json(name = "playCount") val playCount: String?,
    @field:Json(name = "subscribedCount") val subscribedCount: String?,
    @field:Json(name = "tags") val tags: List<String>?,
    @field:Json(name = "createTime") val createTime: Long?,
    @field:Json(name = "subed") val subed: Boolean,
    // 榜单分类
    @field:Json(name = "category") val category: String?,
    // 歌单下歌曲总数
    @field:Json(name = "trackCount") val trackCount: Int?,
    // 歌单类型
    @field:Json(name = "specialType") val specialType: Int?,
    // 歌单创建人Id
    @field:Json(name = "creatorId") val creatorId: String?
)


@JsonClass(generateAdapter = true)
@Keep
data class PlaylistsFromTagDTO(
    @field:Json(name = "hasMore") val hasMore: Boolean,
    @field:Json(name = "records") val records: List<PlaylistDTO>
)

data class RecommendPlaylistDTO(
    @field:Json(name = "recordCount") val recordCount: Int,
    @field:Json(name = "records") val records: List<PlaylistDTO>?
)
