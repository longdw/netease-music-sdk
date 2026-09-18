package com.smart.neteasemusic.sdk.base.config

import com.netease.httpdns.HttpDnsService
import com.netease.httpdns.configuration.DnsOptions
import com.netease.httpdns.listener.RequestStatusListener
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.common.HttpConst
import com.smart.neteasemusic.sdk.base.dns.CMDns
import com.smart.neteasemusic.sdk.base.network.CookieStore
import com.smart.neteasemusic.sdk.base.network.NeThreadPool
import com.smart.neteasemusic.sdk.base.network.interceptor.AccessTokenRefreshInterceptor
import com.smart.neteasemusic.sdk.base.network.interceptor.RequestInterceptor
import com.smart.neteasemusic.sdk.base.network.interceptor.RetryInterceptor
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import okhttp3.ConnectionSpec
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

/**
 * @author david.long 2026/1/6 09:01
 */
internal object NetworkConfig {

    private const val DISPATCHER_NAME = "Netease Music Dispatcher"

    private fun threadFactory(name: String, daemon: Boolean): ThreadFactory {
        return ThreadFactory { runnable ->
            Thread(runnable, name).apply {
                isDaemon = daemon
            }
        }
    }

    internal fun configBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder().apply {
            dispatcher(
                Dispatcher(
                    NeThreadPool.okHttpExecutor(
                        threadFactory(
                            DISPATCHER_NAME,
                            false
                        )
                    )
                )
            )
            readTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            writeTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            connectTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            cookieJar(CookieStore)
            connectionSpecs(listOf(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT
            ))

            //刷新token拦截器
            addInterceptor(AccessTokenRefreshInterceptor())

            //参数加签拦截器
            addInterceptor(RequestInterceptor())

            //日志打印拦截器
            val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
                SDKLogUtil.d("[NetworkConfig] $message")
            }
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(httpLoggingInterceptor)

            //重试拦截器
            val retryCount = SDKContext.sdkConfig?.maxOpenapiRetries?:0
            if (retryCount > 0) {
                addInterceptor(RetryInterceptor(retryCount))
            }
        }
        initDns(builder)
        return builder
    }

    internal fun configTokenBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder().apply {
            dispatcher(
                Dispatcher(
                    NeThreadPool.okHttpExecutor(
                        threadFactory(
                            DISPATCHER_NAME,
                            false
                        )
                    )
                )
            )
            readTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            writeTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            connectTimeout(HttpConst.TIME_OUT, TimeUnit.MILLISECONDS)
            cookieJar(CookieStore)
            connectionSpecs(listOf(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT
            ))

            //参数加签拦截器
            addInterceptor(RequestInterceptor())

            //日志打印拦截器
            val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
                SDKLogUtil.d("[NetworkConfig][configTokenBuilder] $message")
            }
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(httpLoggingInterceptor)

            //重试拦截器
            val retryCount = SDKContext.sdkConfig?.maxOpenapiRetries?:0
            if (retryCount > 0) {
                addInterceptor(RetryInterceptor(retryCount))
            }
        }
        initDns(builder)
        return builder
    }

    private fun initDns(builder: OkHttpClient.Builder) {
        SDKContext.appContext.let {
            val dnsOptions = DnsOptions.Builder().isOpenMergeLocalDNS(true)
                .addHotNameList("openapi.music.163.com")
                .addHotNameList("iot202.music.126.net")
                .addHotNameList("iot201.music.126.net")
                .addHotNameList("iot101.music.126.net")
                .addHotNameList("iot102.music.126.net")
                .setProductKey("3ad737e2a17d4bfca720a6b774c4785c")
                .setHttpTimeOut(HttpConst.TIME_OUT.toInt())
                .setOpenIpv6Request(false)
            HttpDnsService.getInstance().init(it,dnsOptions.build())
            HttpDnsService.getInstance().requestStatusListener = object : RequestStatusListener {
                override fun requestSuccess(
                    url: String,
                    domain: String,
                    responseCode: Int,
                    response: String,
                    hostInfoList: Map<String, List<String>>,
                ) {
                    SDKLogUtil.d("[NetworkConfig] 请求成功 域名 ： $domain ,响应码: $responseCode ,响应内容 ： $response ,hostInfoList:${hostInfoList}")
                }

                override fun requestFailed(domain: String, responseCode: Int, errorMessage: String) {
                    SDKLogUtil.d("[NetworkConfig] 请求失败 域名 ： $domain ,响应码: $responseCode ,失败原因 ： $errorMessage")
                }
            }
            builder.dns(CMDns())
        }
    }
}