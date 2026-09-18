package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索建议结果
 * @author keli.sang 2026/1/23 16:44
 */
@JsonClass(generateAdapter = true)
@Keep
data class SearchSuggestResultDTO(
    /** 提示词列表 */
    @field:Json(name = "suggests") val suggests: List<SearchSuggestKeywordDTO>? = listOf(),
    /** 推荐词列表 */
    @field:Json(name = "recs") val records: List<SearchSuggestKeywordDTO>? = listOf()
)
