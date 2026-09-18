package com.smart.neteasemusic.sdk.player.flow

import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.common.PlayMode

/**
 * @author david.long 2026/3/6 13:30
 */
data class PlayerControlState(
    val playMode: PlayMode = GlobalPlayManager.getPlayMode(),
    val playSpeed: Float = 1.0f
)
