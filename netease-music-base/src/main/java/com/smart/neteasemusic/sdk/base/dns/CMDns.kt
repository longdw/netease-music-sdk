package com.smart.neteasemusic.sdk.base.dns

import android.text.TextUtils
import com.netease.httpdns.HttpDnsService
import com.smart.neteasemusic.base.dns.DsaAddresses
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class CMDns : Dns {
    companion object {
        const val TAG: String = "CMDns"
    }

    override fun lookup(hostname: String): List<InetAddress> {
        var addrs: List<InetAddress> = ArrayList()
        var prefer: String = ""
        SDKLogUtil.d("$hostname >>>>>>>>>>------------------------start-----------------------")
        try {
           if (needHttpDns(hostname)) {
               var ips: MutableList<String> = arrayListOf()
               val ipDomain =
                   HttpDnsService.getInstance().getDomainAvailableCache(domainList(hostname), null)
               if (ipDomain != null && ipDomain.isNotEmpty()) {
                   val domainInfo = ipDomain[hostname]
                   if (domainInfo != null) {
                       ips = domainInfo.availableIps
                       prefer = domainInfo.scorePrefer
                   }
               }
               // 创建 ips 的不可变副本以确保线程安全
               val immutableIps = ArrayList<String>(ips)
               IotDiagnoseDnsRecord.recordHttpDnsResult(hostname, immutableIps)
               if (ips.isNotEmpty()) {
                   SDKLogUtil.d(
                        "HttpDnsHost:$hostname, results: $ips  prefer:$prefer"
                    )
                    addrs = DNSUtils.buildAddressByIps(hostname, ips)
                } else {
                    addrs = getLocalDNS(hostname) // 退避措施，走LocalDNS
                }
            } else {
                addrs = getLocalDNS(hostname)
            }
        } catch (e: UnknownHostException) {
            SDKLogUtil.e("Dns.SYSTEM.lookup UnknownHostException, hostname:$hostname", e)
            addrs = getLocalDNS(hostname)
            if (addrs.isEmpty()) { //解析失败，同时没有兜底IP
                throw e
            }
        } catch (e: IllegalArgumentException) {
//            MUSIC-93784  [Android 云捕] [Crash] at okhttp3.internal.connection.StreamAllocation.okhttp3.internal.http.HttpCodec newStream(okhttp3.OkHttpClient,okhttp3.Interceptor$Chain,boolean)(ProGuard:114) - at okhttp*.internal.connection.St (c7d4e6b48f8406196b8ea98c25434466)
            SDKLogUtil.e("Dns.SYSTEM.lookup IllegalArgumentException, hostname:$hostname", e)
//            throw UnknownHostException(hostname)
        }
        addrs = appendDSAIPs(hostname, addrs)
        val sortedAddrs: MutableList<InetAddress> =
            CallPerformanceManager.getInstance().sort(hostname, addrs, prefer)
        SDKLogUtil.d(
            "$hostname <<<<<<<<<<--------------------- ends ----------------------"
        )
        IotDiagnoseDnsRecord.recordSortDnsResult(
            hostname,
            if (addrs.isEmpty()) null else addrs[0].hostAddress
        )
        return sortedAddrs
    }

    private fun needHttpDns(hostname: String): Boolean {
        val urls = listOf<String>("music.163.com", "music.126.net")
        for (url in urls) {
            if (hostname.contains(url)) {
                return true
            }
        }
        return false
    }

    private fun mergeHttpDnsAndLocalDns(
        httpDnsAddrs: MutableList<InetAddress>,
        hostname: String
    ): MutableList<InetAddress> {
        val mergeResults: MutableList<InetAddress> = ArrayList()
        val localDnsAddrs: MutableList<InetAddress> = ArrayList()
        try {
            localDnsAddrs.addAll(getLocalDNS(hostname))
        } catch (ignored: UnknownHostException) {
        }
        mergeResults.addAll(httpDnsAddrs)
        for (address in localDnsAddrs) {
            if (!mergeResults.contains(address)) {
                mergeResults.add(address)
            }
        }
        return mergeResults
    }

    /**
     * 从localdns获取ip，NoClassDefFoundError是线上低版本手机上的异常，这里处理一下
     * @param hostname 域名
     * @return
     * @throws UnknownHostException
     */
    @Throws(UnknownHostException::class)
    private fun getLocalDNS(hostname: String): List<InetAddress> {
        val startNs = System.nanoTime()
        return try {
            SDKLogUtil.d(
                "Dns.SYSTEM.lookup begin， hostname：" + hostname + "， thread:" + Thread.currentThread().id + ", threadName:" + Thread.currentThread()
                    .toString()
            )
            val localIps = Dns.SYSTEM.lookup(hostname)
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            SDKLogUtil.d(
                "Dns.SYSTEM.lookup success, hostname:" + hostname + " tookMs:" + tookMs + "ms" + "， thread:" + Thread.currentThread().id + ", threadName:" + Thread.currentThread()
                    .toString()
            )
            SDKLogUtil.d("getLocalDNS host:$hostname, localIps: $localIps")
            localIps
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            throw UnknownHostException("NoClassDefFoundError")
        } catch (e: SecurityException) { //http://crash.163.com/dashboard.do#!crashDetail/?appId=A009600174&version=5.2.0_1526012675&crashTypeId=12007387&time=twoMonths
            e.printStackTrace()
            throw UnknownHostException("SecurityException")
        } catch (e: UnknownHostException) {
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            SDKLogUtil.d(
                "Dns.SYSTEM.lookup UnknownHostException, hostname:" + hostname + ", tookMs:" + tookMs + "ms" + ", thread:" + Thread.currentThread().id + ", threadName:" + Thread.currentThread()
                    .toString()
            )
            throw e
        }
    }

    private fun appendDSAIPs(host: String, addresses: List<InetAddress>): List<InetAddress> {
        return appendIPs(host, addresses) { hostname: String ->
            DsaAddresses.lookup(
                hostname
            )
        }
    }

    private fun appendIPs(
        host: String,
        addresses: List<InetAddress>,
        lookup: Function1<String, List<InetAddress>>
    ): List<InetAddress> {
        val result: MutableList<InetAddress> = ArrayList()
        if (addresses != null) {
            result.addAll(addresses)
        }
        val lookupAddresses = lookup.invoke(host)
        for (address in lookupAddresses) {
            if (!result.contains(address)) {
                result.add(address)
            }
        }
        return result
    }


    private fun domainList(domain: String): List<String?> {
        val tempDomain = this.modifyDomain(domain)
        val domainList: ArrayList<String?> = arrayListOf()
        domainList.add(tempDomain)
        return domainList
    }

    private fun modifyDomain(dom: String): String? {
        var domain = dom
        if (TextUtils.isEmpty(domain)) {
            return null
        } else {
            if (domain.contains("http://")) {
                domain = domain.replace("http://", "")
            }

            if (domain.contains("https://")) {
                domain = domain.replace("https://", "")
            }

            return domain
        }
    }
}