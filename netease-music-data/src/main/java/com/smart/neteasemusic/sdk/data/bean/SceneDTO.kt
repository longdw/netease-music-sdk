package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author keli.sang 2026/3/12 15:32
 */
@JsonClass(generateAdapter = true)
@Keep
data class SceneDTO(
    // 场景id
    @field:Json(name = "sceneId") val sceneId: String,
    // 场景名称
    @field:Json(name = "sceneName") val sceneName: String,
    // 场景封面
    @field:Json(name = "coverUrl") val coverUrl: String,
)
