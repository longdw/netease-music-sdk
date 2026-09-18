package com.smart.dhu.testneteasesdk

import android.content.Context
import android.os.Build
import com.smart.neteasemusic.business.NeSdk
import com.smart.neteasemusic.sdk.base.SdkInitCallback
import com.smart.neteasemusic.sdk.base.config.DeviceInfo
import com.smart.neteasemusic.sdk.base.config.SdkConfig
import com.smart.neteasemusic.sdk.base.network.ERROR

/**
 * @author david.long 2026/1/12 09:30
 */
object NeteaseSdkManager {

    private const val rsaKey =
        "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3BMaSxGDoKCCda88l05pwfTOojIeZp6fY+vAQ0a41tHNS45HdzxnI/FfSlIxx2PcW+OvxO2+lz5NK6FgwCwxkfMKGPjDwyLp4SQTCLCtjyb4GvHGfPPFjwHdfJjbJzFHktdXEQ4bFH1V4xIYssP23X5TQAn8G0C6Hv/QlnFO0RjpiA8vSmRnw/gnPB2zi+N9N65TOOV89c9hW8Yksl8oRIbFgSpYSaX8iHXvhf0JQ8oj9n2QAWMLldDhmkzFvhaH8+qc/vxC7VztLqQ2RkcbmFPjWvBQLJAziHCWjlf50KqukY2IW+Cn65FfRcFUZGp/yESgyapLHkBgaO9Luxb/5AgMBAAECggEAEi4JQ576OVVzF/ATnaBNYQsuzHlZJuw+ghE7M5CAi5knM36jiLL/zrUyTBqxcMWqlb+YQFg1GPVrni9CQ1N1oa267PbUibaGv1gRfvBARyNNGwIxDy5QlCYUNYFoQ9dl8yAU4fALPR2Fsf9jjKDwMvztnVQCJj0bwiRRLkM/FVMRa/ePZK0VuhRx6Og/tTyAOWhpyBFrnJQ8YxlMyUkLLyPoI109qgfHbH54uMCkE6N3TRbrTYOsdJZdsH+U5gUGhJ7A65noZS/z8NdK48uVCtQlCy7O8zipqbMxKOm7b1D1Est0lr77NDlt9nYd4+qNcP17FsBqZ99G4z2jbsdIYQKBgQDp1jZtYb9yQ3MQLKrVkPuA1D7tUMGtNYCR1OGusqsatE5j8jnzi76U37bKi6wQOUfDCd6ZOsnXwYce+ajdRFAKkBQgPax5Wqyccfd/1XmtIrNnT8dqjYXqzJ8BBTVMpuiJ9mwR7gtDZnZDSPTPlZPPHYDELIjAb+JFuz9pS0mwLQKBgQDIXYS5HOnrWSWE8hrRJZ+4X7IIwlDvOCrrnepr37O5qIVP0WwP9FM+ZvoIHWFwDmeSGeOKBvSIgVP116tvFQiVZPxUvOiDwEn/AYUMoVXOew6FXPafgE6Vwt/kM53ypTYBIQH0crbfD5F27SYso9VpHH2rWQhkdagM8ZDLni/ifQKBgDMJbhexnfdygUqmdSLn4fzcVl+1oJANfWJW9GsM1AugDvwBgmSFu7MCoQLUOVnRJriLDuqiyistMjCwjQq+PjoP+FSRIXPbNpFuznY4QbZoPH/wCbZX7UKyJoIygM+aHmb+Qdgn/I1Zb7Z/1e741qn95nzqu25E/T37AHwa0pqBAoGBAMALGq5Or0pbXKHLZ7a06BzQ5BSPIwHWhRTmfAy/EkVN59D0Oi4kkTELhHUIrB66P70CQVxHhFPeGXpWJ8PFlj+S1mPl2LqHMKVRcJQFgvfwbJ+Y1e1VDn1ulifk7WvnvBZi67u/w9WS12r0rJU2xtMDctQr9uZK+zLEynLPgLgFAoGBAKBwcI+tKLWDwjQzskHTcPAKMxhqToGsRkibt3im9Oov8pUU2yfjFXPaHHlP5VAg/vMZG0tD1zYMaD5+H4nQiBeeteAjVAS774e6kn/Bof9ierXoyMAxistP7Ghvhmqua0V0Xvh1V+aJI/skPSsVQ8bs6Rop5MfSiSTWEmifshL3"
    private const val appId = "a301010000000000fd85362e77a23672"
    private const val appSecret = "deb22716ac6021bf1987251b37ce8d54"
    private var isInitializing = false
    private var isInit = false
    private const val MAX_CACHE_SIZE = 500L * 1024 * 1024

    fun init(context: Context, callback: SdkInitCallback) {
        if (isInitializing) {
            return
        }
        if (isInit) {
            callback.onSuccess()
            return
        }
        isInitializing = true
        val deviceInfo = DeviceInfo.Builder()
            .channel("smart")
            .deviceId("L6T76P2N6MN999085")
            .deviceType("andrcar")
            .appVer("1.0.0")
            .os("andrcar")
            .osVer(Build.VERSION.RELEASE)
            .brand("smart")
            .model("HX11")
            .clientIp("127.0.0.1")
            .build()
        val sdkConfig = SdkConfig.Builder()
            .appId(appId)
            .appSecret(appSecret)
            .rsaPriKey(rsaKey)
            .deviceInfo(deviceInfo)
            .setPlayTimeOut(10)
            .setMaxCacheSize(MAX_CACHE_SIZE)
            .openPlayerLog(true)
            .openSdkLog(true)
            .build()

        NeSdk.init(context, sdkConfig, object: SdkInitCallback {
            override fun onSuccess() {
                LogUtil.e("初始化成功")
                isInitializing = false
                isInit = true
                callback.onSuccess()
            }

            override fun onError(error: ERROR?) {
                LogUtil.e("初始化失败-->$error")
                isInitializing = false
                callback.onError(error)
            }

        })
    }
}