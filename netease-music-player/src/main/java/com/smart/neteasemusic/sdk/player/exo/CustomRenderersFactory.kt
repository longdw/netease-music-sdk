package com.smart.neteasemusic.sdk.player.exo

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink

/**
 * @author david.long 2026/3/24 15:02
 */
@OptIn(UnstableApi::class)
internal class CustomRenderersFactory(
    context: Context,
) : DefaultRenderersFactory(context) {
    init {
        setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
//        setExtensionRendererMode(EXTENSION_RENDERER_MODE_OFF)
    }

    private val audioProcessor by lazy {
        KaraokeAudioProcessor()
    }

    /**
     * setEnableFloatOutput控制DefaultAudioSink是否允许以32-bit浮点PCM（ENCODING_PCM_FLOAT）格式向AudioTrack输出
     * DefaultAudioSink 执行流程如下：
     *
     * 1. DefaultAudioSink先查询硬件是否支持 PCM_FLOAT 直接输出
     *          ↓
     * 车机硬件上报：支持 ENCODING_PCM_FLOAT 直通
     *          ↓
     * 2. DefaultAudioSink 决定走硬件 float 直通路径
     *          ↓
     * 3. AudioProcessor 链的 configure() 被跳过
     *          ↓
     * 4. onConfigure 不会被调用
     */

    override fun buildAudioSink(
        context: Context,
        enableFloatOutput: Boolean,
        enableAudioTrackPlaybackParams: Boolean,
        enableOffload: Boolean
    ): AudioSink {
        //构建底层 AudioSink，并将我们的 Processor 注入到音频处理链中
        return DefaultAudioSink.Builder(context)
            .setAudioProcessors(arrayOf<AudioProcessor>(audioProcessor))
            //验证AudioTrack前的数据
//            .setAudioProcessors(arrayOf<AudioProcessor>(teeAudioProcessor))
            .setEnableFloatOutput(false)
            .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
            .build()
    }

//    override fun buildAudioSink(
//        context: Context,
//        enableFloatOutput: Boolean,
//        enableAudioOutputPlaybackParams: Boolean
//    ): AudioSink {
//
//        //构建底层 AudioSink，并将我们的 Processor 注入到音频处理链中
//        return DefaultAudioSink.Builder(context)
//            .setAudioProcessors(arrayOf<AudioProcessor>(audioProcessor))
//            .setEnableFloatOutput(false)
//            .setEnableAudioTrackPlaybackParams(enableAudioOutputPlaybackParams)
//            .build()
//    }

    fun setVocalVolume(volume: Float) {
        audioProcessor.setVocalVolume(volume)
    }

    fun setBgmVolume(volume: Float) {
        audioProcessor.setBgmVolume(volume)
    }

//    private val outputDir = File(context.getExternalFilesDir(null), "pcm_probe").apply {
//        mkdirs()
//    }
//
//    private val teeAudioProcessor by lazy {
//        TeeAudioProcessor(
//            TeeAudioProcessor.WavFileAudioBufferSink(
//                File(outputDir, "pcm_probe").absolutePath
//            )
//        )
//    }
}