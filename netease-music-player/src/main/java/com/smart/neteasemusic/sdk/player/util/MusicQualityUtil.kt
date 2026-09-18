package com.smart.neteasemusic.sdk.player.util

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.common.NeSPConst
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.bean.MusicQualityBitrateValue
import com.smart.neteasemusic.sdk.player.PlayerProxy

/**
 * 音质相关工具类
 *
 * 音质概念：
 * 1. 真实码率：歌曲的实际码率
 * 2. 虚拟码率(VirtualBitrate)：歌曲的码率档位枚举 [MusicQualityBitrateValue]
 * 3. 音质等级：[MusicQualityBitrateValue] 里对应的值，和虚拟码率档位一一对应
 *
 * 音质设置说明：
 * 1. 播放列表音质：
 *    - 通过 [saveQualityAndPlay] 设置的音质会应用于当前播放列表的所有歌曲
 *    - 切换音质时会保持当前播放进度
 *    - 同一播放列表中的所有歌曲使用相同的音质设置
 *
 * 2. 全局音质设置：
 *    - 通过 [setPreferQuality] 只更新 SharedPreferences 中的音质设置
 *    - 这个设置会影响新加载的播放列表的默认音质
 *    - 不会影响当前正在播放的列表
 *
 * 特殊音质说明：
 * 1. Hi-res：
 *    - 是一种特殊的高品质音质，本质上是 SQ（无损）
 *    - 具有更高的采样率和位宽
 *    - 由于其特殊性，无法仅通过码率判断
 *    - 为保持兼容性，Hi-res 歌曲的码率统一设为 [HIRES_BITRATE]
 *
 * 2. 自动音质：
 *    - 值为 [AudioQualityValue.AUTO_BITRATE]
 *    - 系统会根据网络状况自动选择合适的音质
 *
 * @see MusicQualityBitrateValue 码率档位定义
 */
@Keep
internal object MusicQualityUtil {

    /**
     * 获取sp里存储的"在线播放优先音质"对应的value：1、2、3...
     * @return quality 音质，取值参考 [MusicQualityBitrateValue]
     */
    fun getPlayQualityValue(): Int {
        // 默认音质 标准音质 LQ_BITRATE
        return SpStorage.getInt(
            NeSPConst.KEY_PLAY_QUALITY,
            MusicQualityBitrateValue.LQ_BITRATE.qualityValue
        )
    }

    /**
     * 切换全局默认播放音质
     * @param quality 目标音质，取值参考 [MusicQualityBitrateValue] 中的qualityValue
     */
    fun setPreferQuality(quality: Int) {
        saveQualityAndPlay(quality, false)
    }

    /**
     * 切换当前歌曲的音质
     * @param quality 目标音质，取值参考 [MusicQualityBitrateValue] 中的qualityValue
     */
    fun setQuality(quality: Int) {
        saveQualityAndPlay(quality)
    }

    /**
     * 切换当前播放列表的音质并更新播放状态
     *
     * 此方法会：
     * 1. 更新 [SpStorage] 中的音质设置
     * 2. 将当前播放列表中所有歌曲切换到指定音质
     * 3. 保持当前播放进度继续播放
     *
     * @param quality 目标音质，取值参考 [MusicQualityBitrateValue] 中的qualityValue
     * @param autoPlay 切换后的播放状态控制
     *        - true: 切换后强制播放
     *        - false: 切默认音质
     */
    private fun saveQualityAndPlay(quality: Int, autoPlay: Boolean = true) {
        SDKLogUtil.i("[MusicQuality][saveQualityAndPlay] quality=$quality, autoPlay=$autoPlay")
        val preQuality = getPlayQualityValue()
        SDKLogUtil.i("[MusicQuality][saveQualityAndPlay] preQuality=$preQuality")
        if (preQuality == quality) {
            return
        }
        SpStorage.putInt(NeSPConst.KEY_PLAY_QUALITY, quality)

        //更新播放列表音质，这样做的目的是在重新播放前，在请求播放url时会用到MusicInfo#getAudioBitrate()信息
        //2026.6.22 PlayableMusicInfoCache中put保存缓存时改用同一个MusicInfo对象，之前copy保存不同对象有bug（如果歌单中只有一首歌，收藏歌曲后切换，歌曲变成未收藏）
        //所以要去掉copy，但由此引发的音质切换问题需要修复
        val audioBitrateValue = MusicQualityBitrateValue.fromQuality(quality)
        val level = audioBitrateValue.level
        val bitrate = audioBitrateValue.bitrate.toLong()

        PlayerProxy.getCurrentMusicList().forEach {
            it.level = level
            it.br = bitrate
        }

        //注意 PlayableMusicInfoCache 中的缓存不要清除，因为音质可能还会切回来，这样缓存保留还是有意义的
//        PlayableMusicInfoCache.clear()

        if (autoPlay) {
            autoChangePlayQuality(quality, preQuality)
        }
    }

    private fun autoChangePlayQuality(targetQuality: Int, fallbackQuality: Int) {
        PlayerProxy.replayForPlayQuality(targetQuality, fallbackQuality)
    }
}