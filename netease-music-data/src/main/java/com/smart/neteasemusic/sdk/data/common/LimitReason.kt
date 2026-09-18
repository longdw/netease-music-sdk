package com.smart.neteasemusic.sdk.data.common

import androidx.annotation.Keep

/**
 * 不可播放原因
 *
 * 用于标识声音的状态, 有值代表声音应该被过滤掉
 * @author david.long 2026/1/14 09:30
 */
@Keep
enum class LimitReason(val value: String) {
    /** 他人待审核的声音 */
    NOT_PUBLISH("filterNotPublish"),

    /** 收藏了别人的被删除声音 */
    DELETED("filterDelete"),

    /** 过滤删除&下架 */
    DELETE_AND_UNDER_SHELF("filterDeleteAndUnderShelf"),

    /** 过滤节目状态 */
    INVALID_PROGRAM_STATUS("filterDJProgramStatus"),

    /** 过滤隐私状态 */
    PRIVATE_PROGRAM("filterPrivilegeProgram"),

    /** 过滤电台状态 */
    INVALID_RADIO_STATUS("filterDJRadioStatus"),

    /** 过滤付费 */
    NEED_PAY("filterFee"),

    /** 兼容旧版本的禁止播放状态 */
    FORBIDDEN("forbidden");

    companion object {
        /**
         * 获取播放限制的原因描述
         * @param playFlog 播放标记
         * @return String 限制原因描述
         */
        @JvmStatic
        fun getLimitReason(playFlog: String?): String? {
            val reason = values().find { it.value == playFlog }
            return when (reason) {
                NOT_PUBLISH -> "内容待审核"
                DELETED -> "内容已删除"
                DELETE_AND_UNDER_SHELF -> "内容已下架"
                INVALID_PROGRAM_STATUS -> "节目状态异常"
                PRIVATE_PROGRAM -> "私密内容"
                INVALID_RADIO_STATUS -> "电台状态异常"
                NEED_PAY -> "需要付费"
                FORBIDDEN -> "禁止播放"
                null -> null
            }
        }
    }
}