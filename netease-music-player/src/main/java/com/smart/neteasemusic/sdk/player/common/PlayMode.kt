package com.smart.neteasemusic.sdk.player.common

/**
 * @author david.long 2026/1/20 13:27
 */
enum class PlayMode(val value: Int) {
    /**
     * 列表循环
     */
    LOOP(1),

    /**
     * 随机播放
     */
    RANDOM(2),

    /**
     * 单曲循环
     */
    SINGLE(3),

    /**
     * 顺序播放
     */
    ORDER(4);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: LOOP
    }
}