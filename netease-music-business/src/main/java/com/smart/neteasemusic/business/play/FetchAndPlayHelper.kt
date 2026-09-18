package com.smart.neteasemusic.business.play

import com.smart.neteasemusic.sdk.base.util.NetWorkUtil
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.player.PlayState
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import kotlinx.coroutines.launch

/**
 * 配合快捷播放按钮使用
 * @author david.long 2026/5/8 13:19
 */
object FetchAndPlayHelper {
    fun init() {
        SmartScope.launchUI {
            launch {
                PlayerFlows.playStateStateFlow.collect { playState ->
                    SDKLogUtil.i("[FetchAndPlayHelper][init] playerState=$playState")
                    if (playState == PlayState.PLAYING) {
                        ShortcutPlayManager.playing()
                    } else if (playState == PlayState.PAUSED) {
                        ShortcutPlayManager.pause()
                    }
                    //这里如果加上，当进程杀了重新打开app时，会导致ShortcutPlayManager中获取playSourceId为空
//                    else if (playState == PlayState.PREPARED) {
//                        ShortcutPlayManager.prepared()
//                    }
                }
            }

            launch {
                PlayerFlows.switchMusicStateFlow.collect {
                    if (!NetWorkUtil.isNetworkConnected()) {
                        ShortcutPlayManager.pause()
                    }
                }
            }
        }
    }
}