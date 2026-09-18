package com.smart.neteasemusic.sdk.base.focus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil

/**
 * 音频焦点管理
 * @author david.long 2026/1/29 13:44
 */
class AudioFocusManager(
    private val onAudioFocusChangeCallback: OnAudioFocusChangeCallback
) : AudioManager.OnAudioFocusChangeListener {
    private val audioManager: AudioManager? = SDKContext.appContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var focusRequest: AudioFocusRequest? = null
    private var audioAttributes: AudioAttributes? = null

    @Volatile
    private var haveFocus = false

    /**
     * 请求音频焦点
     */
    fun requestFocus(): Boolean {
        if (null == focusRequest) {
            if (audioAttributes == null) {
                audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            }
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes!!)
                .setWillPauseWhenDucked(false)
                .setOnAudioFocusChangeListener(this)
                .build()
        }
        val requestAudioFocusResult = audioManager?.requestAudioFocus(focusRequest!!)
        SDKLogUtil.i("[AudioFocusManager][requestFocus] 音频焦点请求结果: " + (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED))
        return requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    /**
     * 释放音频焦点
     */
    fun releaseAudioFocus() {
        try {
            focusRequest?.let {
                audioManager?.abandonAudioFocusRequest(it)
                focusRequest = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        SDKLogUtil.i("[AudioFocusManager][onAudioFocusChange] 音频焦点发生变化： $focusChange")
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                setHaveFocus(true)
                SDKLogUtil.i("[AudioFocusManager][onAudioFocusChange] AUDIOFOCUS_GAIN")
                onAudioFocusChangeCallback.onGain()
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，会触发此回调事件
                //例如播放QQ音乐，网易云音乐等
                //此时应当暂停音频并释放音频相关的资源。
                setHaveFocus(false)
                SDKLogUtil.i("[AudioFocusManager][onAudioFocusChange] AUDIOFOCUS_LOSS")
                // 失去audio focus很长一段时间，必须停止所有的audio播放，清理资源
                //onAudioFocusChange长时间失去音频焦点，停止播放或者降低音量，释放资源
                onAudioFocusChangeCallback.onFocusLoss()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，会触发此回调事件
                //例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                setHaveFocus(false)
                SDKLogUtil.i("[AudioFocusManager][onAudioFocusChange] AUDIOFOCUS_LOSS_TRANSIENT")
                // 暂时失去audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
                onAudioFocusChangeCallback.onLossTransient()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                //短暂性丢失焦点并作降音处理，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK时，会触发此回调事件
                //通常需要降低音量
                //8.0以后降音处理系统默认做了，不需要手动处理
                //如果不希望系统自动降低音量，而是想自己控制，
                //这个可以通过AudioFocusRequest.Builder.setWillPauseWhenDucked(true)方法取消系统的默认行为，
                //然后通过监听音频焦点变化，来自己处理
                setHaveFocus(false)
                SDKLogUtil.i("[AudioFocusManager][onAudioFocusChange] AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放，也可降低音量。
                onAudioFocusChangeCallback.onLossTransientCanDuck()
            }
        }
    }

    private fun setHaveFocus(haveFocus: Boolean) {
        this.haveFocus = haveFocus
    }

    fun isHaveFocus(): Boolean {
        return haveFocus
    }

    /**
     * 焦点变化回调
     */
    interface OnAudioFocusChangeCallback {
        /**
         * 焦点丢失
         */
        fun onFocusLoss()

        fun onLossTransient()

        fun onLossTransientCanDuck()

        fun onGain()
    }
}