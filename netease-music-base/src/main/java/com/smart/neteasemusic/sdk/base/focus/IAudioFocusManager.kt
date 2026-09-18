package com.smart.neteasemusic.sdk.base.focus

/**
 * @author david.long 2026/1/29 13:21
 */
interface IAudioFocusManager {
    /**
     * 申请焦点
     */
    fun requestAudioFocus(): Boolean

    /**
     * 释放焦点
     */
    fun releaseAudioFocus()

    /**
     * 是否有音频焦点
     */
    fun hasAudioFocus(): Boolean
}