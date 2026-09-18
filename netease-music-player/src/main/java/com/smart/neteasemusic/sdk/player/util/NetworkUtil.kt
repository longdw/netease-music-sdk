package com.smart.neteasemusic.sdk.player.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.Keep

/**
 * @author david.long 2026/3/17 10:09
 */
object NetworkUtil {
    fun getNetworkType(context: Context?): NetworkType {
        if (isEthernet(context)) {
            return NetworkType.ETHERNET
        }
        val info: NetworkInfo? = getConnectivityService(context)?.activeNetworkInfo
        return if (info != null && info.isConnected) {
            when (info.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    NetworkType.WIFI
                }

                ConnectivityManager.TYPE_MOBILE -> {
                    NetworkType.CELLULAR

                }

                else -> {
                    NetworkType.OTHER
                }
            }
        } else {
            NetworkType.NONE
        }
    }

    private fun isEthernet(context: Context?): Boolean {
        val cm =
            getConnectivityService(context)
                ?: return false
        val info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) ?: return false
        val state = info.state ?: return false
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING
    }

    private fun getConnectivityService(context: Context?): ConnectivityManager? {
        return context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }
}

/**
 * 网络类型枚举类
 */
@Keep
enum class NetworkType(val label: String) {
    CELLULAR("cellular"),
    WIFI("wifi"),
    OTHER("other"),
    NONE("none"),
    ETHERNET("ethernet"),
    UNKNOWN("unknown")
}