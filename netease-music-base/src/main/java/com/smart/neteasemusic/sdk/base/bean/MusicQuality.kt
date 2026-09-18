package com.smart.neteasemusic.sdk.base.bean

import com.smart.neteasemusic.sdk.base.common.VipType


/**
 * 业务侧可以直接使用来展示
 * @param value 对应 [com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue] 中的 qualityValue
 * @param des 音质对应的中文名称
 * @param vipType 当前音质需要最低会员类型
 * @param size 音质需要流量大小
 * @author david.long 2026/1/29 15:44
 */
open class MusicQuality(
    open val value: Int,
    open val des: String,
    open val vipType: Int = VipType.FREE,
    open val size: Long
)
