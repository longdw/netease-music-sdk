package com.smart.neteasemusic.sdk.base.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @author david.long 2026/1/26 10:33
 */
fun Int.constrainValue(min: Int, max: Int): Int {
    return min.coerceAtLeast(this.coerceAtMost(max))
}

/**
 * 包含系统栏的完整屏幕尺寸
 */
fun Context.screenSize(): Pair<Int, Int> {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(displayMetrics)
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    } else {
        val windowMetrics = wm.maximumWindowMetrics
        val bounds = windowMetrics.bounds
        Pair(bounds.width(), bounds.height())
    }
}