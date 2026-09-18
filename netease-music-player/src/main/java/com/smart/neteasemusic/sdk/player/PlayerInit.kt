package com.smart.neteasemusic.sdk.player

import com.smart.neteasemusic.sdk.base.flow.loginStateFlow
import com.smart.neteasemusic.sdk.base.login.LoginState
import com.smart.neteasemusic.sdk.base.util.SmartScope

/**
 * @author david.long 2026/2/9 18:28
 */
object PlayerInit {
    fun init() {
        //监听登录状态变化并根据当前的播放状态来处理相关逻辑
        SmartScope.launchUI {
            loginStateFlow.collect { loginState ->
                if (loginState is LoginState.Authenticated) {
                    //要清空缓存的url
                    if (GlobalPlayManager.isPlaying()) {
                        //并重新播放
                        GlobalPlayManager.replay()
                    } else if (GlobalPlayManager.isPause()) {
                        //重新获取歌曲信息
                        PlayerProxy.reInitMusicInfo()
                    }
                }
            }
        }
        PlayerProxy.init()
    }
}