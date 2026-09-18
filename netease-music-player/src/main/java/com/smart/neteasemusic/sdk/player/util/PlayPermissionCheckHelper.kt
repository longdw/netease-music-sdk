package com.smart.neteasemusic.sdk.player.util

import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.common.PlayError

/**
 * 起播前检查是否能播放
 * @author david.long 2026/1/29 09:30
 */
object PlayPermissionCheckHelper {

    /**
     * 检查歌曲是否能播放
     */
    fun check(musicInfo: MusicInfo): Int {
        if (musicInfo.isCanPlay) {
            return PlayError.NONE
        }
        //无版权
        if (!musicInfo.hasCopyRight) {
            return PlayError.NO_COPYRIGHT
        }
        //需要VIP才能播放
        if (musicInfo.vipPlayFlag) {
            return PlayError.ONLY_VIP_CAN_PLAY
        }
        //需要付费才能播放
        if (musicInfo.payPlayFlag) {
            return PlayError.PAY_CAN_PLAY
        }
        if (!musicInfo.isCanTrail) {
            return PlayError.SONG_CAN_NOT_PLAY
        }
        return PlayError.NONE
    }

    /**
     * 检查是否所有歌曲都不能播放
     */
    fun isAllCannotPlay(musicList: List<MusicInfo>): Boolean {
        if (musicList.isEmpty()) {
            return true
        }
        for (musicInfo in musicList) {
            if (musicInfo.isCanPlay) {
                return false
            } else if (musicInfo.isCanTrail) {
                return false
            }
        }
        return true
    }

}