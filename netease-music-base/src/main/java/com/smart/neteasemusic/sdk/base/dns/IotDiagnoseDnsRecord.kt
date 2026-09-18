package com.smart.neteasemusic.sdk.base.dns

import androidx.annotation.Keep
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Keep
object IotDiagnoseDnsRecord {

    private val httpDnsResult: ConcurrentMap<String, List<String>> = ConcurrentHashMap()
    private val sortDnsResult: ConcurrentMap<String, String?> = ConcurrentHashMap()

    fun recordHttpDnsResult(host: String, dnsResults: List<String>) {
        if (dnsResults.isEmpty()) return
        try {
            val immutableCopy = Collections.unmodifiableList(ArrayList(dnsResults))
            httpDnsResult[host] = immutableCopy
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun recordSortDnsResult(host: String, sortResult: String?) {
        if (sortResult.isNullOrEmpty()) return
        try {
            sortDnsResult[host] = sortResult
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取HTTP DNS解析结果
     * 返回副本以避免外部修改
     */
    fun getHttpDnsResult(host: String): List<String>? {
        val result = httpDnsResult[host]
        // 即使原始列表是不可变的，返回副本也是更安全的做法
        return result?.let { ArrayList(it) }
    }

    /**
     * 获取排序后的IP
     */
    fun getPingIp(host: String): String? {
        return sortDnsResult[host]
    }
}