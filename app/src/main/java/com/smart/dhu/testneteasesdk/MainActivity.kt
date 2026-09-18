package com.smart.dhu.testneteasesdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smart.neteasemusic.business.play.FetchAndPlayHelper
import com.smart.neteasemusic.business.preload.PlayerPreLoadManager
import com.smart.neteasemusic.sdk.base.SdkInitCallback
import com.smart.neteasemusic.sdk.base.network.ERROR

/**
 * @author david.long 2026/7/22 17:02
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        LogUtil.init("NeteaseMusicTest")

        NeteaseSdkManager.init(this, object: SdkInitCallback {
            override fun onSuccess() {
                LogUtil.e("初始化状态：成功")

                FetchAndPlayHelper.init()
                //开启预加载
                PlayerPreLoadManager.init()
            }

            override fun onError(error: ERROR?) {
                LogUtil.e("初始化状态：失败")
            }

        })
    }
}