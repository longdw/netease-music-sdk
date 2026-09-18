package com.smart.neteasemusic.sdk.base.config

/**
 * @author david.long 2026/1/9 10:22
 */
class DeviceInfo private constructor(builder: Builder){

    /**
     * 厂商标识，由云音乐分配，需线下联系云音乐同事确认
     */
    val channel: String

    /**
     * 设备唯一Id（每台设备需保持唯一，可传sn、mac、IMEI、vin、iccid等值）
     */
    val deviceId: String

    /**
     * 设备类型，需线下联系云音乐产品确认
     */
    val deviceType: String

    /**
     * 客户端版本号（自研的云音乐版本号）
     */
    val appVer: String

    /**
     * 操作系统类型，由云音乐分配，需线下联系云音乐同事确认
     */
    val os: String

    /**
     * 操作系统版本（ 车机系统，如：8.1.0）
     */
    val osVer: String

    /**
     * 品牌，需线下联系云音乐同事确认
     */
    val brand: String

    /**
     * 设备型号（车型、产品型号）
     */
    val model: String

    /**
     * 终端IP，设备调用接口时的IP地址
     */
    val clientIp: String

    init {
        this.channel = builder.channel
        this.deviceId = builder.deviceId
        this.deviceType = builder.deviceType
        this.appVer = builder.appVer
        this.os = builder.os
        this.osVer = builder.osVer
        this.brand = builder.brand
        this.model = builder.model
        this.clientIp = builder.clientIp
    }

    class Builder {
        var channel: String = ""
            private set
        var deviceId: String = ""
            private set
        var deviceType: String = ""
            private set
        var appVer: String = ""
            private set
        var os: String = ""
            private set
        var osVer: String = ""
            private set
        var brand: String = ""
            private set
        var model: String = ""
            private set
        var clientIp: String = ""
            private set

        fun channel(channel: String) = apply {
            this.channel = channel
        }

        fun deviceId(deviceId: String) = apply {
            this.deviceId = deviceId
        }

        fun deviceType(deviceType: String) = apply {
            this.deviceType = deviceType
        }

        fun appVer(appVer: String) = apply {
            this.appVer = appVer
        }

        fun os(os: String) = apply {
            this.os = os
        }

        fun osVer(osVer: String) = apply {
            this.osVer = osVer
        }

        fun brand(brand: String) = apply {
            this.brand = brand
        }

        fun model(model: String) = apply {
            this.model = model
        }

        fun clientIp(clientIp: String) = apply {
            this.clientIp = clientIp
        }

        fun build() = DeviceInfo(this)
    }
}