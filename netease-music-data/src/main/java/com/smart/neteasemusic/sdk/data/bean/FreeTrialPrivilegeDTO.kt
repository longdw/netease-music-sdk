package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 试听权限信息
 *
 * @property resConsumable 当前用户下，歌曲维度是否支持全曲试听（除了歌曲信息，还取自会员控制）
 * @property userConsumable 用户维度是否支持全曲试听
 * @property cannotListenReason 不可试听的原因代码：
 *                             - 0: 不展示原因
 *                             - 1: 曲库资源维度不可听
 *                             - 2: 单场景单日歌曲数量限制
 *                             - 3: 单场景单日单歌次数限制
 *                             - 4: 单歌跨天不能听鼾天的周期
 * @property listenType 试听类型：
 *                     - 0：不支持全曲试听
 *                     - 1：仅仅能够试听一次
 *                     - 2：试听24小时
 *                     - 3：永久可听
 *
 * 说明：
 * 1. 当 resConsumable 和 userConsumable 同时为 true 时，表示可以全曲试听
 * 2. 全曲试听时，对应的 FreeTrail 返回为空
 * 3. 非全曲试听时，FreeTrail 包含具体的试听时间段信息
 * 4. 当用户无播放权益，且用户非vip时，歌曲全曲试听的次数大于0且在歌曲允许的试听时间段内
 *    且用户无地区版权封禁，则取自会员侧用户全曲试听信息
 * @author david.long 2026/1/14 09:20
 */
@JsonClass(generateAdapter = true)
@Keep
data class FreeTrialPrivilegeDTO(
    @field:Json(name = "resConsumable") val resConsumable: Boolean = false,
    @field:Json(name = "userConsumable") val userConsumable: Boolean = false,
    @field:Json(name = "cannotListenReason") val cannotListenReason: Int = 0,
    @field:Json(name = "listenType") val listenType: Int = 0,
) {
    /**
     * 判断是否可以全曲试听
     * @return true 表示可以全曲试听，false 表示不可以全曲试听或只能部分试听
     */
    fun isFullTrailAvailable(): Boolean = resConsumable && userConsumable

    /**
     * 获取试听类型描述
     * @return 试听类型的文字描述
     */
    fun getListenTypeDesc(): String = when (listenType) {
        0 -> "不支持全曲试听"
        1 -> "仅可试听一次"
        2 -> "可试听24小时"
        3 -> "永久可听"
        else -> "未知试听类型"
    }

    /**
     * 获取不可试听原因描述
     * @return 不可试听原因的文字描述
     */
    fun getCannotListenReasonDesc(): String = when (cannotListenReason) {
        0 -> "无限制"
        1 -> "曲库资源维度不可听"
        2 -> "单场景单日歌曲数量限制"
        3 -> "单场景单日单歌次数限制"
        4 -> "单歌跨天不能听限制"
        else -> "未知原因"
    }
}