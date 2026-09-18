package com.smart.neteasemusic.sdk.base.bean

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * 试听时间段信息
 * 注：仅在非全曲试听时有效，全曲试听时该字段为空
 *
 * @property start 试听开始时间（单位：秒）
 * @property end 试听结束时间（单位：秒）
 * @author david.long 2026/1/14 09:21
 */
@JsonClass(generateAdapter = true)
@Keep
data class FreeTrail(
    //试听开始时间
    var start: Int,
    //试听结束时间
    var end: Int
)