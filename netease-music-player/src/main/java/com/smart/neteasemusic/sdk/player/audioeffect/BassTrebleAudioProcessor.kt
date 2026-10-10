package com.smart.neteasemusic.sdk.player.soundeffect.processor

import android.annotation.SuppressLint
import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.audio.BaseAudioProcessor
import com.smart.neteasemusic.sdk.player.soundeffect.dsp.BiquadFilter
import com.smart.neteasemusic.sdk.player.soundeffect.meta.Bt
import java.nio.ByteBuffer
import kotlin.math.max

/**
 * 低音/高音处理器。PDF：bass/treble 单位 dB、范围 ±12；on 由 bass/treble 任一非 0 推导。
 * 拐点频率 PDF 没给，沿用 200Hz / 4kHz（可调）。
 *
 * 改动：shelf 用 Q 形式（0.7 ≈ 最平坦）；参数更新继承滤波器状态；
 * 增加自动 preamp（正增益时把总响应峰值压回 0dB，避免削波）；
 * 非 16-bit 时返回 NOT_SET 而不是抛异常，与其它处理器一致。
 * @author david.long 2026/9/18 13:29
 */
@SuppressLint("UnsafeOptInUsageError")
class BassTrebleAudioProcessor : BaseAudioProcessor() {

    companion object {
        private const val BASS_FREQ = 200f
        private const val TREBLE_FREQ = 4000f
        private const val SHELF_Q = 0.7f
        /** 0 = 不做 preamp（与原厂响度一致，默认）；1 = 按响应峰值压回 0dB（bass=+12 时会降 12dB，不推荐） */
        private const val PREAMP_COMPENSATION = 0.0
        private const val GAIN_MIN = -12f
        private const val GAIN_MAX = 12f
        private val EMPTY_SNAPSHOT = Snapshot(null, emptyArray(), emptyArray(), 0, 1f)
    }

    private class Snapshot(
        val bt: Bt?,
        val bassFilters: Array<BiquadFilter>,
        val trebleFilters: Array<BiquadFilter>,
        val channelCount: Int,
        val preamp: Float,
    )

    @Volatile private var snapshot: Snapshot = EMPTY_SNAPSHOT
    private var configuredSampleRate = 0
    private var configuredChannelCount = 0

    @Synchronized
    fun updateParam(bt: Bt?) {
        if (configuredSampleRate > 0 && configuredChannelCount > 0) rebuild(bt, configuredSampleRate, configuredChannelCount)
        else snapshot = Snapshot(bt, emptyArray(), emptyArray(), 0, 1f)
    }

    override fun onConfigure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) return AudioProcessor.AudioFormat.NOT_SET
        configuredSampleRate = inputAudioFormat.sampleRate
        configuredChannelCount = inputAudioFormat.channelCount
        rebuild(snapshot.bt, configuredSampleRate, configuredChannelCount)
        return inputAudioFormat
    }

    @Synchronized
    private fun rebuild(bt: Bt?, sr: Int, ch: Int) {
        if (bt == null) { snapshot = Snapshot(null, emptyArray(), emptyArray(), ch, 1f); return }
        val bassGain = bt.bass.coerceIn(GAIN_MIN, GAIN_MAX)
        val trebleGain = bt.treble.coerceIn(GAIN_MIN, GAIN_MAX)
        val old = snapshot
        val bass = Array(ch) { i ->
            BiquadFilter().apply {
                configureLowShelf(sr, BASS_FREQ, SHELF_Q, bassGain)
                old.bassFilters.getOrNull(i)?.let { copyStateFrom(it) }
            }
        }
        val treble = Array(ch) { i ->
            BiquadFilter().apply {
                configureHighShelf(sr, TREBLE_FREQ, SHELF_Q, trebleGain)
                old.trebleFilters.getOrNull(i)?.let { copyStateFrom(it) }
            }
        }
        // 两个 shelf 的级联响应峰值（96 个对数频点）
        var peakDb = 0.0
        for (k in 0 until 96) {
            val f = 20.0 * Math.pow(0.45 * sr / 20.0, k / 95.0)
            peakDb = max(peakDb, bass[0].magnitudeDb(sr, f) + treble[0].magnitudeDb(sr, f))
        }
        snapshot = Snapshot(bt, bass, treble, ch, Math.pow(10.0, -peakDb * PREAMP_COMPENSATION / 20.0).toFloat())
    }

    override fun queueInput(inputBuffer: ByteBuffer) {
        // Media3 会用 EMPTY_BUFFER 调用 queueInput（排空/结束流）。replaceOutputBuffer(0) 此时返回的
        // 正是同一个 EMPTY_BUFFER，后面的 put(inputBuffer) 就会变成 src==this 而抛 IllegalArgumentException。
        if (!inputBuffer.hasRemaining()) return
        val snap = snapshot
        val outputBuffer = replaceOutputBuffer(inputBuffer.remaining())
        val bt = snap.bt
        // PDF：bass、treble 任一非 0 才算启用
        val active = bt != null && bt.on && (bt.bass != 0f || bt.treble != 0f) && snap.bassFilters.isNotEmpty()
        if (!active) {
            outputBuffer.put(inputBuffer)
            outputBuffer.flip()
            return
        }
        val channelCount = snap.channelCount.coerceAtLeast(1)
        var ch = 0
        while (inputBuffer.remaining() >= 2) {
            var v = inputBuffer.short / 32768f * snap.preamp
            v = snap.bassFilters[ch].process(v)
            v = snap.trebleFilters[ch].process(v)
            outputBuffer.putShort((v * 32768f).toInt().coerceIn(-32768, 32767).toShort())
            if (++ch == channelCount) ch = 0
        }
        outputBuffer.flip()
    }

    override fun onFlush() {
        snapshot.bassFilters.forEach { it.reset() }
        snapshot.trebleFilters.forEach { it.reset() }
    }

    override fun onReset() {
        configuredSampleRate = 0
        configuredChannelCount = 0
        snapshot = EMPTY_SNAPSHOT
    }
}

