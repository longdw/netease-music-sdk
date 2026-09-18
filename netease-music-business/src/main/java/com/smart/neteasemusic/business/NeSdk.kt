package com.smart.neteasemusic.business

import android.content.Context
import com.smart.neteasemusic.business.recorder.DataRecordManager
import com.smart.neteasemusic.sdk.base.NeSdkInitManager
import com.smart.neteasemusic.sdk.base.SdkInitCallback
import com.smart.neteasemusic.sdk.base.common.PlayType
import com.smart.neteasemusic.sdk.base.config.SdkConfig
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.PlayerInit

/**
 * SDK初始化
 * 注意：整个流程是异步的过程
 * @author david.long 2026/1/6 14:02
 */
object NeSdk {

    val isInitialized
        get() = NeSdkInitManager.isInitialized

    fun init(context: Context, sdkConfig: SdkConfig, callback: SdkInitCallback? = null) {
        NeSdkInitManager.init(context, sdkConfig, object: SdkInitCallback {
            override fun onSuccess() {
                //初始化播放器
                PlayerInit.init()
                //初始化数据记录
                DataRecordManager.init()
                //初始化播放倍速
                if (sdkConfig.openDefaultFeature) {
                    initPlaySpeed()
                }

                callback?.onSuccess()
            }

            override fun onError(error: ERROR?) {
                callback?.onError(error)
            }

        })
    }

    /**
     * 初始化播放倍速
     */
    private fun initPlaySpeed() {
        if (GlobalPlayManager.getPlayType() == PlayType.VOICE) {
            val playSpeed = GlobalPlayManager.getPlaySpeed()
            GlobalPlayManager.setPlaySpeed(playSpeed)
        } else {
            GlobalPlayManager.setPlaySpeed(1.0f)
        }
    }
}