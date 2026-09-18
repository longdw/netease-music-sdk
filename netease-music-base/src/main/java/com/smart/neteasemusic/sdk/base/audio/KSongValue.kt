package com.smart.neteasemusic.sdk.base.audio

import androidx.annotation.Keep

/**
 * @author david.long 2026/1/14 10:20
 */
@Keep
enum class KSongValue(val value: Int) {
    /**
     * 关
     */
    OFF(0),

    /**
     * 开
     */
    ON(1);

    @Keep
    companion object {
        @JvmStatic
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: OFF
    }
}