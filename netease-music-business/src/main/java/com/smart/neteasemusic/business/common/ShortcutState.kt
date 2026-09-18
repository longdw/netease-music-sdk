package com.smart.neteasemusic.business.common

/**
 * 快捷按钮的状态
 * @author david.long 2026/2/25 14:24
 */
sealed class ShortcutState {

    object NONE : ShortcutState()
    object LOADING : ShortcutState()
    object PLAYING: ShortcutState()
    object PREPARED: ShortcutState()
    object PAUSE: ShortcutState()
    data class ERROR(val errorCode: Int? = null, val errorMsg: String? = null, val e: Throwable? = null): ShortcutState()
}