package com.smart.neteasemusic.sdk.base.config

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.focus.IAudioFocusManager

/**
 * @author david.long 2026/1/6 13:28
 */
@Keep
class SdkConfig private constructor(builder: Builder){

    val appId: String
    val appSecret: String
    val maxOpenapiRetries: Int
    val rsaPriKey: String
    val deviceInfo: DeviceInfo?
    val playTimeOut: Long
    val supportDolby: Boolean
    val customAudioFocusManager: IAudioFocusManager?
    val maxCacheSize: Long
    /** 播放器log是否打开，一般信息info级别输出，错误日志error级别输出 */
    val openPlayerLog: Boolean
    /** sdk log是否打开，一般信息info级别输出，错误日志error级别输出 */
    val openSdkLog: Boolean
    /** 默认特性是否打开 */
    val openDefaultFeature: Boolean

    init {
        this.appId = builder.appId
        this.appSecret = builder.appSecret
        this.maxOpenapiRetries = builder.maxOpenapiRetries
        this.rsaPriKey = builder.rsaPriKey
        this.deviceInfo = builder.deviceInfo
        this.playTimeOut = builder.playTimeOut
        this.supportDolby = builder.supportDolby
        this.customAudioFocusManager = builder.customAudioFocusManager
        this.maxCacheSize = builder.maxCacheSize
        this.openPlayerLog = builder.openPlayerLog
        this.openSdkLog = builder.openSdkLog
        this.openDefaultFeature = builder.openDefaultFeature
    }

    @Keep
    class Builder {
        var appId: String = ""
            private set
        var appSecret: String = ""
            private set
        var maxOpenapiRetries: Int = 3
            private set
        var rsaPriKey: String = ""
            private set
        var deviceInfo: DeviceInfo? = null
            private set
        var playTimeOut: Long = 10L
            private set
        var supportDolby: Boolean = true
            private set
        var customAudioFocusManager: IAudioFocusManager? = null
            private set
        var maxCacheSize: Long = 0L
            private set
        var openPlayerLog: Boolean = true
            private set
        var openSdkLog: Boolean = true
            private set
        var openDefaultFeature: Boolean = true
            private set

        /**
         * 开放平台申请的appId
         */
        fun appId(appId: String) = apply {
            this.appId = appId
        }

        /**
         * 开放平台申请的appSecret
         */
        fun appSecret(appSecret: String) = apply {
            this.appSecret = appSecret
        }

        /**
         * 开放平台申请的rsaPriKey
         *  [生成方式参考](https://developer.music.163.com/st/developer/document?docId=56fb9f543d6f4cc3b48a862ed408a075)
         */
        fun rsaPriKey(rsaPriKey: String) = apply {
            this.rsaPriKey = rsaPriKey
        }

        /**
         * openapi相关接口重试最大次数
         * 默认3次
         */
        fun setOpenapiMaxRetries(maxRetries: Int) = apply {
            this.maxOpenapiRetries = maxRetries
        }

        fun deviceInfo(deviceInfo: DeviceInfo) = apply {
            this.deviceInfo = deviceInfo
        }

        /**
         * 设置播放歌曲拉流的网络超时时间，时间单位 秒
         */
        fun setPlayTimeOut(timeOut: Long) = apply {
            this.playTimeOut = timeOut
        }

        /**
         * 是否支持杜比音效
         */
        fun supportDolby(support: Boolean) = apply {
            this.supportDolby = support
        }

        /**
         * 自定义音频焦点管理
         */
        fun customAudioFocusManager(audioFocusManager: IAudioFocusManager) = apply {
            this.customAudioFocusManager = audioFocusManager
        }

        /**
         * 设置最大的缓存大小（单位：字节）
         * 默认500M
         */
        fun setMaxCacheSize(maxCacheSize: Long) = apply {
            this.maxCacheSize = maxCacheSize
        }

        /**
         * 是否打印播放器log
         * 默认打开
         */
        fun openPlayerLog(openPlayerLog: Boolean) = apply {
            this.openPlayerLog = openPlayerLog
        }

        /**
         * 是否打印sdk log
         * 默认打开
         */
        fun openSdkLog(openSdkLog: Boolean) = apply {
            this.openSdkLog = openSdkLog
        }

        /**
         * 是否打开默认特性
         * （比如目前支持的特性：只有播客有播放倍速，所以播放类型变化会重置播放倍速，sdk初始化完成会初始化倍速。后续此开关还可以扩展）
         * 默认开启
         */
        fun openDefaultFeature(openDefaultFeature: Boolean) = apply {
            this.openDefaultFeature = openDefaultFeature
        }

        fun build() = SdkConfig(this)
    }

}