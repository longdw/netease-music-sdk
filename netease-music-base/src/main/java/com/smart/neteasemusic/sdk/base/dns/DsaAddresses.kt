package com.smart.neteasemusic.base.dns

import okhttp3.Dns.Companion.SYSTEM
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * 用于主站的DSA加速，详细参考文档
 * http://doc.hz.netease.com/pages/viewpage.action?pageId=256531701
 * Created by yuxiaofei on 2020/12/11
 */
object DsaAddresses {
    private const val DSA_SUFFIX = ".163jiasu.com"
    private const val DSA_CONFIG_KEY = "ipStrategy#dsa_config"
    private const val appReleaseDomain="music.163.com"
    private val matchHosts: MutableSet<String> = mutableSetOf()
    private val lock: Lock = ReentrantLock()

    init {
        matchHosts.add("interface.$appReleaseDomain")
        matchHosts.add("interface3.$appReleaseDomain")
//        parseRemoteConfig()
    }

//    private fun parseRemoteConfig() {
//        try {
//            val dsaJSONArray: JSONArray? = ServiceFacade.get(ICustomConfig::class.java)?.getAppCustomConfig(
//                    NeteaseMusicConst.APP_KEY_CONFIG.CLOUD_MUSIC_KEY, null as JSONArray?, DSA_CONFIG_KEY, "dsaHosts")
//            dsaJSONArray?.forEach {
//                if (it is String) {
//                    matchHosts.add(it)
//                }
//            }
//        } catch (e: ClassCastException) {
//            e.printStackTrace()
//        }
//    }

    /**
     * 这个方法将查找域名对应的dsa加速域名，并分会加速域名的LocalDNS解析结果
     */
    fun lookup(hostname: String?): List<InetAddress> {
        val needDsa = true
        if (!needDsa) {
            return mutableListOf()
        }
        if (lock.withLock {matchHosts.contains(hostname) }) {
            val dsaHost = hostname + DSA_SUFFIX
            return doLocalDNS(dsaHost)
        }
        return mutableListOf()
    }

    private fun doLocalDNS(dsaHost: String): List<InetAddress> {
        try {
            return SYSTEM.lookup(dsaHost)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return mutableListOf()
    }
}