package com.smart.neteasemusic.sdk.data.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/3/18 14:22
 */
@JsonClass(generateAdapter = true)
data class PlaylistDetailDTO(
    // 歌单Id
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "describe") val describe: String?,
    @field:Json(name = "coverImgUrl") val coverImgUrl: String?,
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
