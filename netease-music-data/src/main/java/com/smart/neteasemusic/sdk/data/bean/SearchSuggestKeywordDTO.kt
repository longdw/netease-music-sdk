package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索建议词
 * @author keli.sang 2026/1/23 16:46
 */
@JsonClass(generateAdapter = true)
@Keep
data class SearchSuggestKeywordDTO(
    /** 提示词 */
    @field:Json(name = "keyword") val keyword: String? = "",
    /** 高亮信息 */
    @field:Json(name = "highLightInfo") val highLightInfo: String? = "",
    /** 提示词前 icon 图片地址 */
    @field:Json(name = "iconUrl") val iconUrl: String? = "",
    /** 提示词后标签图片地址 */
    @field:Json(name = "tagUrl") val tagUrl: String? = "",
    /** 提示词对应资源类型 */
    @field:Json(name = "resourceType") val resourceType: String? = "",
    /** 提示词对应资源 id */
    @field:Json(name = "resourceId") val resourceId: String? = "",
    /** 提示词对应实际资源名称 */
    @field:Json(name = "resourceName") val resourceName: String? = "",
    /** 提示词对应资源展示名称 */
    @field:Json(name = "showText") val showText: String? = "",
)
