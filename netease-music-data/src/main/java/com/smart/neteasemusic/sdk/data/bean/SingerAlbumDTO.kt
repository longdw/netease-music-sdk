package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * @author chenLin 2026/5/28 14:29
 */
@JsonClass(generateAdapter = true)
@Keep
data class SingerAlbumDTO(
    val page: AlbumPageDTO,
    val records: List<AlbumDTO>
)

@JsonClass(generateAdapter = true)
@Keep
data class AlbumPageDTO(
    val current: Int,
    val size: Int,
    val recordCount: Int,
    val total: Int?
)