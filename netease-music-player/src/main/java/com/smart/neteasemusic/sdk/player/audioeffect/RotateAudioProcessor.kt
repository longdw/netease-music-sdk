package com.smart.neteasemusic.sdk.player.soundeffect.processor

import android.annotation.SuppressLint
import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.audio.BaseAudioProcessor
import com.smart.neteasemusic.sdk.player.soundeffect.meta.Rotate
import java.nio.ByteBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.abs
import kotlin.math.exp

/**
 * 声场旋转（Rotate）音效处理器，对应 [Rotate] 参数。
 *
 * ## 效果原理
 * 把每一帧的 (L, R) 采样看成一个二维向量，随时间以恒定角速度做等功率旋转
 * （标准二维旋转矩阵）：
 * ```
 * outL = L*cosθ - R*sinθ
 * outR = L*sinθ + R*cosθ
 * ```
 * θ 随采样点数量线性增长，听感上就是声音持续在左右声道之间"转圈"。
 *
 * [Rotate.velocity] 约定为旋转速度，单位 **度/秒**。例如 velocity=20 表示
 * 大约 18 秒转一圈（360/20）。如果实际产品定义的单位不是这个（转/分钟、Hz 等），
 * 只需要改 [degreesPerSecondToAngularVelocity] 这一个换算函数，不用动别的逻辑。
 *
 * 目前只处理双声道输入；非双声道（mono / 多声道环绕）直接透传 —— "旋转"这个概念
 * 建立在左右声道成对存在的基础上，多声道环绕旋转需要单独设计（比如按扬声器环绕顺序
 * 做相邻声道渐变混合），超出当前范围。
 * @author david.long 2026/9/20 16:57
 */
@SuppressLint("UnsafeOptInUsageError")
class RotateAudioProcessor : BaseAudioProcessor() {

    companion object {
        private const val HEAD_RADIUS_M = 0.0875
        private const val SPEED_OF_SOUND = 343.0
        private const val CONTROL_INTERVAL = 32      // 每 32 帧更新一次 ITD/滤波系数（相位变化极慢）
        private const val SIDE_KEEP = 0.5f
        private const val FADE_IN_SECONDS = 0.05f
        private const val BUF_MASK = 127             // 延迟线 128 点，足够 96kHz 下 0.65ms
        private val EMPTY_SNAPSHOT = Snapshot(null, 0)
    }

    private class Snapshot(val rotate: Rotate?, val channelCount: Int)

    @Volatile private var snapshot: Snapshot = EMPTY_SNAPSHOT
    private var configuredSampleRate = 0
    private var configuredChannelCount = 0

    // 以下仅播放线程访问
    private var phaseRad = 0.0
    private val delayBuf = FloatArray(BUF_MASK + 1)
    private var writeIdx = 0
    private var lpL = 0f
    private var lpR = 0f
    private var mix = 0f
    private var ctrlCount = 0
    private var itdL = 0f
    private var itdR = 0f
    private var gainL = 1f
    private var gainR = 1f
    private var lpCoefL = 0f
    private var lpCoefR = 0f

    @Synchronized
    fun updateParam(rotate: Rotate?) {
        snapshot = Snapshot(rotate, configuredChannelCount)
    }

    override fun onConfigure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) return AudioProcessor.AudioFormat.NOT_SET
        configuredSampleRate = inputAudioFormat.sampleRate
        configuredChannelCount = inputAudioFormat.channelCount
        snapshot = Snapshot(snapshot.rotate, configuredChannelCount)
        clearDspState()
        return inputAudioFormat
    }

    private fun degreesPerSecondToRadiansPerSample(dps: Float): Double =
        if (configuredSampleRate <= 0) 0.0 else Math.toRadians(dps.toDouble()) / configuredSampleRate

    private fun updateControl(sr: Int) {
        val sinPhi = sin(phaseRad)
        val cosPhi = cos(phaseRad)
        val beta = asin(sinPhi)                                   // 侧向角，前后折叠到 ±90°
        val itdSec = HEAD_RADIUS_M / SPEED_OF_SOUND * (beta + sinPhi)
        val itdSamples = (abs(itdSec) * sr).toFloat()
        val shadow = abs(sinPhi).toFloat()
        val rear = (-cosPhi).coerceAtLeast(0.0).toFloat()
        val rearScale = 1f - 0.5f * rear                          // 转到脑后：高频再降

        val nearFc = 16000f * rearScale
        val farFc = (16000f - 13000f * shadow) * rearScale
        val farGain = 1f - 0.3f * shadow

        // sinPhi>0：声源在右侧 -> 左耳是远耳、且更晚到达
        val rightSide = sinPhi >= 0
        itdL = if (rightSide) itdSamples else 0f
        itdR = if (rightSide) 0f else itdSamples
        gainL = if (rightSide) farGain else 1f
        gainR = if (rightSide) 1f else farGain
        val fcL = if (rightSide) farFc else nearFc
        val fcR = if (rightSide) nearFc else farFc
        lpCoefL = exp(-2.0 * PI * fcL / sr).toFloat()
        lpCoefR = exp(-2.0 * PI * fcR / sr).toFloat()
    }

    private fun readDelayed(delay: Float): Float {
        val pos = writeIdx - 1 - delay          // writeIdx-1 是最新样本
        val i0 = Math.floor(pos.toDouble()).toInt()
        val frac = pos - i0
        val a = delayBuf[i0 and BUF_MASK]
        val b = delayBuf[(i0 + 1) and BUF_MASK]
        return a + (b - a) * frac
    }

    override fun queueInput(inputBuffer: ByteBuffer) {
        // Media3 会用 EMPTY_BUFFER 调用 queueInput（排空/结束流）。replaceOutputBuffer(0) 此时返回的
        // 正是同一个 EMPTY_BUFFER，后面的 put(inputBuffer) 就会变成 src==this 而抛 IllegalArgumentException。
        if (!inputBuffer.hasRemaining()) return
        val snap = snapshot
        val outputBuffer = replaceOutputBuffer(inputBuffer.remaining())
        val rotate = snap.rotate
        if (rotate == null || !rotate.on || snap.channelCount != 2 || configuredSampleRate <= 0) {
            mix = 0f
            outputBuffer.put(inputBuffer)
            outputBuffer.flip()
            return
        }

        val sr = configuredSampleRate
        val step = degreesPerSecondToRadiansPerSample(rotate.velocity)
        val mixStep = 1f / (FADE_IN_SECONDS * sr)

        while (inputBuffer.remaining() >= 4) {
            val l = inputBuffer.short / 32768f
            val r = inputBuffer.short / 32768f

            if (ctrlCount == 0) updateControl(sr)
            if (++ctrlCount >= CONTROL_INTERVAL) ctrlCount = 0

            val mid = (l + r) * 0.5f
            val side = (l - r) * 0.5f
            delayBuf[writeIdx and BUF_MASK] = mid
            writeIdx = (writeIdx + 1) and BUF_MASK

            var earL = readDelayed(itdL)
            var earR = readDelayed(itdR)
            lpL = earL * (1f - lpCoefL) + lpL * lpCoefL
            lpR = earR * (1f - lpCoefR) + lpR * lpCoefR
            earL = lpL * gainL
            earR = lpR * gainR

            val wetL = earL + side * SIDE_KEEP
            val wetR = earR - side * SIDE_KEEP
            if (mix < 1f) mix = (mix + mixStep).coerceAtMost(1f)
            val outL = l + (wetL - l) * mix
            val outR = r + (wetR - r) * mix

            outputBuffer.putShort((outL * 32768f).toInt().coerceIn(-32768, 32767).toShort())
            outputBuffer.putShort((outR * 32768f).toInt().coerceIn(-32768, 32767).toShort())

            phaseRad += step
            if (phaseRad >= 2 * PI) phaseRad -= 2 * PI else if (phaseRad < 0) phaseRad += 2 * PI
        }
        outputBuffer.flip()
    }

    private fun clearDspState() {
        delayBuf.fill(0f)
        writeIdx = 0
        lpL = 0f; lpR = 0f
        ctrlCount = 0
    }

    /** seek 时只清滤波/延迟状态，保留相位，避免声源位置突然跳变 */
    override fun onFlush() = clearDspState()

    override fun onReset() {
        configuredSampleRate = 0
        configuredChannelCount = 0
        phaseRad = 0.0
        mix = 0f
        clearDspState()
        snapshot = EMPTY_SNAPSHOT
    }
}
