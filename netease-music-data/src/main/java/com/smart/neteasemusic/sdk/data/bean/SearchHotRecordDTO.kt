package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索热词
 * @author keli.sang 2026/1/23 16:41
 */
@JsonClass(generateAdapter = true)
@Keep
data class SearchHotRecordDTO(
    @field:Json(name = "keyword") val keyword: String? = "",
)

