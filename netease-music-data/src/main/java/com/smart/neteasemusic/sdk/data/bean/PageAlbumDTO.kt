package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/1/23 17:12
 */
@JsonClass(generateAdapter = true)
@Keep
data class PageAlbumDTO(
    /** 分页信息 */
    @field:Json(name = "page") val page: PageInfoDTO? = null,
    /** 专辑列表 */
    @field:Json(name = "records") val records: List<AlbumDTO>? = null
)
@JsonClass(generateAdapter = true)
@Keep
data class PageAlbumMusicDTO(
    /** 专辑内分页信息 */
    @field:Json(name = "page") val page: PageInfoDTO? = null,
    /** 专辑内歌曲列表 */
    @field:Json(name = "records") val records: List<MusicInfoDTO>? = null
)
