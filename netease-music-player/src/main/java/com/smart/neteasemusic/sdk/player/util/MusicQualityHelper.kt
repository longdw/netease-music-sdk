package com.smart.neteasemusic.sdk.player.util

import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue
import com.smart.neteasemusic.sdk.base.common.VipType
import com.smart.neteasemusic.sdk.base.user.UserManager
import com.smart.neteasemusic.sdk.player.common.PlayError
import com.smart.neteasemusic.sdk.base.bean.MusicQuality
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.player.GlobalPlayManager

/**
 * 业务端使用此类来切换音质
 * @author david.long 2026/2/2 10:29
 */
object MusicQualityHelper {

    /**
     * 获取当前播放的音质
     */
    fun getCurrentQuality(): Int {
        return MusicQualityUtil.getPlayQualityValue()
    }

    /**
     * 获取当前音质的中文描述
     * 业务端要求不同，显示的名字不同，此处仅供参考
     */
    fun getCurrentQualityDes(): String {
        val currentQuality = getCurrentQuality()
        return parseQualityToDes(currentQuality)
    }

    /**
     * 根据音质[com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue] 中的 qualityValue获取描述
     */
    fun parseQualityToDes(quality: Int): String {
        return MusicQualityBitrateValue.fromQuality(quality).readName
    }

    /**
     * 根据[com.smart.neteasemusic.sdk.data.bean.MusicInfo.level]级别获取描述
     */
    fun parseLevelToDes(level: String?): String {
        val qualityValue = MusicQualityBitrateValue.fromLevel(level).qualityValue
        return parseQualityToDes(qualityValue)
    }

    /**
     * 切换全局默认播放音质
     * @param quality 目标音质，取值参考 [MusicQualityBitrateValue] 中的qualityValue
     */
    fun setPreferQuality(musicQuality: MusicQuality): Int {
        SDKLogUtil.i("[MusicQualityHelper][setPreferQuality] 准备切换音质：[$musicQuality]")
        SDKLogUtil.i("[MusicQualityHelper][setPreferQuality] 用户信息：[${UserManager.profile}]")
        return when {
            musicQuality.vipType == VipType.FREE || UserManager.isSVip() -> {
                MusicQualityUtil.setPreferQuality(musicQuality.value)
                PlayError.NONE
            }

            musicQuality.vipType == VipType.BLACK_VIP && UserManager.isBlackVip() -> {
                MusicQualityUtil.setQuality(musicQuality.value)
                PlayError.NONE
            }

            !UserManager.isLogin() -> {
                PlayError.NEED_LOGIN
            }

            musicQuality.vipType == VipType.BLACK_VIP -> {
                PlayError.NEED_BLACK_VIP
            }

            else -> {
                PlayError.NEED_S_VIP
            }
        }
    }

    /**
     * 设置音质
     * @return [PlayError]
     */
    fun setQuality(musicQuality: MusicQuality): Int {
        SDKLogUtil.i("[MusicQualityHelper][setQuality] currentMusicInfo=${GlobalPlayManager.getCurrentMusicInfo()}")
        SDKLogUtil.i("[MusicQualityHelper][setQuality] before:${GlobalPlayManager.getCurrentMusicInfo()?.getAudioBitrate()} after：[$musicQuality]")
        SDKLogUtil.i("[MusicQualityHelper][setQuality] 用户信息：[${UserManager.profile}]")
        return when {
            musicQuality.vipType == VipType.FREE ||
            UserManager.isSVip() ||
            (musicQuality.vipType == VipType.BLACK_VIP && UserManager.isBlackVip()) -> {
                MusicQualityUtil.setQuality(musicQuality.value)
                PlayError.NONE
            }

            !UserManager.isLogin() -> {
                PlayError.NEED_LOGIN
            }

            musicQuality.vipType == VipType.BLACK_VIP -> {
                PlayError.NEED_BLACK_VIP
            }

            else -> {
                PlayError.NEED_S_VIP
            }
        }
    }
}