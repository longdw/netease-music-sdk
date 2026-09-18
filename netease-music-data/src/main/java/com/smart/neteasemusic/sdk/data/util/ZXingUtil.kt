package com.smart.neteasemusic.sdk.data.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Paint
import android.text.TextUtils
import androidx.annotation.Keep
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.google.zxing.qrcode.encoder.Encoder
import java.util.EnumMap
import java.util.Hashtable
import kotlin.collections.set

/**
 * @author david.long 2026/1/16 09:49
 */
object ZXingUtil {
    /**
     * 生成二维码
     *
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小
     * @param colorCode 二维码颜色值(如:0xff000000)
     * @param colorBackground 二维码背景颜色值(如:0xffffffff)
     * @return bitmap
     */
    fun encodeAsBitmap(
        contents: String?,
        size: Int,
        colorCode: Int = BLACK,
        colorBackground: Int = WHITE
    ): Bitmap? {
        return try {
            val hints: Hashtable<EncodeHintType, String?> = Hashtable()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            val bitMatrix = QRCodeWriter().encode(
                contents,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] =
                        if (bitMatrix[x, y]) colorCode else colorBackground
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 可控制内边距的二维码
     *
     * @param contents 需要生成二维码的文字、网址等
     * @param characterSet 字符串编码
     * @param width 需要生成二维码的宽度
     * @param height 需要生成二维码的高度
     * @param margin 二维码外边距
     * @param errorCorrectionLevel 容错率
     * @param colorCode 二维码颜色值(如:0xff000000)
     * @param colorBackground 二维码背景颜色值(如:0xffffffff)
     * @return bitmap
     */
    @Keep
    fun createQRCodeBitmap(
        contents: String?,
        characterSet: String = "UTF-8",
        width: Int,
        height: Int,
        margin: Int = 1,
        errorCorrectionLevel: ErrorCorrectionLevel? = ErrorCorrectionLevel.L,
        colorCode: Int = BLACK,
        colorBackground: Int = WHITE
    ): Bitmap? {
        var marginArg = margin
        if (TextUtils.isEmpty(contents) || width <= 0 || height <= 0) {
            return null
        }
        marginArg = 0.coerceAtLeast(marginArg)
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = characterSet
        try {
            val qrCode = Encoder.encode(contents, errorCorrectionLevel, hints)
            val input: ByteMatrix = qrCode.matrix ?: return null
            return renderBitmap(input, width, height, marginArg, colorCode, colorBackground)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun renderBitmap(
        input: ByteMatrix,
        width: Int,
        height: Int,
        margin: Int,
        codeColor: Int,
        backColor: Int
    ): Bitmap {
        val inputWidth = input.width
        val inputHeight = input.height
        val qrWidth = inputWidth + (margin shl 1)
        val qrHeight = inputHeight + (margin shl 1)
        val outputAspectRatio = 1f * width / height
        val qrAspectRatio = 1f * qrWidth / qrHeight
        val offsetX: Int
        val offsetY: Int
        val multiple: Float
        when {
            outputAspectRatio == qrAspectRatio -> {
                offsetX = 0
                offsetY = 0
                multiple = 1f * width / qrWidth
            }
            outputAspectRatio < qrAspectRatio -> {
                offsetX = 0
                offsetY = (height - width / qrAspectRatio).toInt() shr 1
                multiple = 1f * width / qrWidth
            }
            else -> {
                offsetX = (width - height * qrAspectRatio).toInt() shr 1
                offsetY = 0
                multiple = 1f * height / qrHeight
            }
        }
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawColor(backColor)
        val paint = Paint()
        paint.color = codeColor
        paint.style = Paint.Style.FILL
        for (y in 0 until inputHeight) {
            val top = offsetY + (y + margin) * multiple
            for (x in 0 until inputWidth) {
                if (input[x, y].toInt() == 1) {
                    val left = offsetX + (x + margin) * multiple
                    canvas.drawRect(left, top, left + multiple, top + multiple, paint)
                }
            }
        }
        return output
    }
}