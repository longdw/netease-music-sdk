package com.smart.neteasemusic.sdk.base.dns

import android.content.Context
import androidx.annotation.Keep
import com.netease.httpdns.configuration.ThreadPool
import com.netease.httpdns.util.NetworkUtil
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import java.net.Inet4Address
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.iterator

/**
 * @author iBiandev
 * 请求监控数据的记录器，记录着host域名对应的IP列表分数Map<Network></Network>, Map<Host></Host>, Map<IP></IP>, MARK>>>
 */
@Keep
class CallPerformanceRecorder {
    private val sp by lazy {
        SDKContext.appContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
    private val ipMap: ConcurrentHashMap<String, Float> by lazy {
        val all = sp?.all ?: HashMap<String, Float>()
        val result = ConcurrentHashMap<String, Float>()
        for (k in all.keys) {
            result.put(k, all.get(k) as? Float ?: 0F)
        }
        result
    }

    /**
     * 将host对应的ip列表，根据跑马排序
     *
     * @param host 相应的域名
     * @param raw  httpdns原始返回的ip顺序
     */
    fun sort(host: String, raw: List<InetAddress?>?, prefer: String): List<InetAddress?>? {
        if (raw == null) {
            return raw
        }

        // 将列表分为IPv4和IPv6两部分
        val (ipv4Addresses, ipv6Addresses) = raw.filterNotNull().partition { it is Inet4Address }

        // 定义排序比较器
        val comparator = Comparator<InetAddress> { o1, o2 ->
            val markOne = ipMap[getKey(o1.hostAddress)] ?: INIT_MARK
            val markTwo = ipMap[getKey(o2.hostAddress)] ?: INIT_MARK
            (markTwo - markOne).let {
                when {
                    it > 0 -> 1
                    it < 0 -> -1
                    else -> 0
                }
            }
        }

        // 根据prefer参数决定排序顺序
        val sortedList = when (prefer.lowercase()) {
            "ipv4" -> ipv4Addresses.sortedWith(comparator) + ipv6Addresses.sortedWith(comparator)
            "ipv6" -> ipv6Addresses.sortedWith(comparator) + ipv4Addresses.sortedWith(comparator)
            else -> (ipv4Addresses + ipv6Addresses).sortedWith(comparator)
        }

        // 记录日志
        val sb = StringBuilder()
        sortedList.forEach { address ->
            sb.append("ip:${address.hostAddress}, mark:${ipMap[getKey(address.hostAddress)]}\n")
        }
        SDKLogUtil.d(sb.toString())

        return sortedList
    }

    /**
     * 添加一条ip的请求记录
     * @param mark 得分，和之前的分支累加
     */
    fun putRecord(host: String, ip: String, mark: Float): Float {
        val key = getKey(ip)
        val rawMark = ipMap.get(key) ?: INIT_MARK
        val newMark = if (rawMark == null) mark else rawMark + mark
        val finalMark = Math.max(MIN_MARK, Math.min(newMark, MAX_MARK))
        return ipMap.put(key, finalMark) ?: INIT_MARK
    }

    private fun getKey(ip: String): String {
        val networkType = try {
            NetworkUtil.getNetworkType().split("_")[0]
        } catch (e: Exception) {
            ""
        }
        return networkType + "_" + ip
    }

    fun saveData() {
        ThreadPool.schedule({
            val sp = sp?.edit() ?: return@schedule
            for (k in ipMap.keys()) {
                val v = ipMap.get(k)
                if (v != INIT_MARK && v != null) {
                    sp.putFloat(k, v)
                }
            }
            sp.apply()
        }, 0)
    }

    companion object {
        private const val TAG = "CallPerformanceRecorder"
        protected const val INIT_MARK = 0.0f
        protected const val MAX_MARK = 100.0f
        protected const val MIN_MARK = -100.0f
    }
}