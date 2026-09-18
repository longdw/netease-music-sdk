package com.smart.neteasemusic.business.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.DynamicLayout
import android.text.Layout
import android.text.TextPaint
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnLayout
import com.smart.neteasemusic.business.R
import com.smart.neteasemusic.business.bean.LyricData
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.SmartScope
import com.smart.neteasemusic.sdk.data.lyric.LyricLoadState
import com.smart.neteasemusic.sdk.data.lyric.LyricManager
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.flow.PlayerFlows
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 歌词控件
 * @author david.long 2026/2/26 16:58
 */
//滚动歌词后，时间线保留时间
private const val TIMELINE_KEEP_TIME = 2 * 1000L
private const val NO_LYRIC_HINT = "网易云音乐"
private const val SCROLL_DURATION = 300L
private const val ADJUST_DURATION = 100L
//歌词控件点击监听器 x(点击坐标x，相对于控件) y(点击坐标y，相对于控件)
typealias OnTapListener = (view: LyricView, x: Float, y: Float) -> Unit

//播放按钮点击监听器，点击后应该跳转到指定播放位置
//返回值表示 是否成功消费该事件，如果成功消费，则会更新UI
//time(选中播放进度)
typealias OnPlayClickListener = (view: LyricView, time: Int) -> Boolean

class LyricView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {

    private var fetchLyricJob: Job? = null
    private val lyricDataList = mutableListOf<LyricData>()
    private var scrollAnimator: ValueAnimator? = null
    private var highlightAnimator: ValueAnimator? = null
    private var isLifecyclePause = false
    private var onTapListener: OnTapListener? = null
    private var onPlayClickListener: OnPlayClickListener? = null

    /** 画布的偏移量，歌词就是靠这个来实现的移动 */
    private var offset = 0f
    /** 当前播放的行 */
    private var currentShowLine = 0
    private var currentTextSize = 46f
    private var normalTextSize = 36f
    private var normalTextColor = Color.LTGRAY
    private var currentTextColor = Color.BLACK
    /** 滑动歌词，中间线所指向的歌词的颜色 */
    private var selectedTextColor = Color.DKGRAY
    private val dividerHeight = 0

    /** 给歌词添加阴影 */
    private var lyricShadowColor = Color.TRANSPARENT
    private var lyricShadowRadius = 0f
    private var lyricShadowDx = 0f
    private var lyricShadowDy = 0f

    private val lyricPaint = TextPaint().apply {
        isAntiAlias = true
        //歌词按放大后的来计算
        textSize = currentTextSize
    }

    /** 时间线相关参数 */
    //是否显示中间的时间线
    private var timelineTextSize = 30f
    private var isShowTimeline = false
    private var timelinePaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = timelineTextSize
        textAlign = Paint.Align.CENTER
        strokeWidth = 1f
        strokeCap = Paint.Cap.ROUND
    }
    private var timelineColor = Color.LTGRAY
    private var timelineTimeColor = Color.LTGRAY
    private var timelinePlayDrawable = ContextCompat.getDrawable(context, R.drawable.ic_lyric_play)
    private val timelineTimeWidth = 100f
    /** 歌词右边播放按钮的大小 */
    private val timelinePlayDrawableSize = 60f
    private val timelineFontMetrics = timelinePaint.fontMetrics

    //配合computeScroll()来完成平滑滚动
    private val scroller = Scroller(context)
    private var isFling = false
    private var isTouching = false
    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }
    //是否是第一次初始化
    private var isFirstInit = true

    init {
        if (attrs != null) {
            getContext().withStyledAttributes(attrs, R.styleable.LyricView) {
                for (i in 0 until indexCount) {
                    when (val attr: Int = getIndex(i)) {
                        R.styleable.LyricView_lyricTextSize -> {
                            setLyricTextSize(getDimension(attr, normalTextSize))
                            val zoom = getFloat(R.styleable.LyricView_lyricHighLightTextZoom, 1.0f)
                            setLyricHighLightTextZoom(zoom)
                        }
                        R.styleable.LyricView_lyricTimeLineColor -> {
                            timelineColor = getColor(attr, timelineColor)
                        }
                        R.styleable.LyricView_lyricNormalTextColor -> {
                            normalTextColor = getColor(attr, normalTextColor)
                        }
                        R.styleable.LyricView_lyricSelectedTextColor -> {
                            selectedTextColor = getColor(attr, selectedTextColor)
                        }
                        R.styleable.LyricView_lyricHighLightTextColor -> {
                            currentTextColor = getColor(attr, currentTextColor)
                        }
                        R.styleable.LyricView_lyricTimeLinePlayDrawable -> {
                            timelinePlayDrawable = getDrawable(attr)
                        }
                        R.styleable.LyricView_lyricShadow -> {
                            val lyricShadow = getBoolean(attr, false)
                            if (lyricShadow) {
                                lyricShadowColor = getColor(R.styleable.LyricView_lyricShadowColor, lyricShadowColor)
                                lyricShadowRadius = getFloat(R.styleable.LyricView_lyricShadowRadius, lyricShadowRadius)
                                lyricShadowDx = getFloat(R.styleable.LyricView_lyricShadowDx, lyricShadowDx)
                                lyricShadowDy = getFloat(R.styleable.LyricView_lyricShadowDy, lyricShadowDy)
                                updateLyricShadow()
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        offset = height / 2f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            isTouching = false
            // 手指离开屏幕，启动延时任务，恢复歌词位置
            if (hasLyric() && isShowTimeline) {
                adjustCenter()
                postDelayed(hideTimelineRunnable, TIMELINE_KEEP_TIME)
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    private val hideTimelineRunnable = Runnable {
        if (hasLyric() && isShowTimeline) {
            isShowTimeline = false
            smoothScrollToLine(currentShowLine)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        listenData()
//        fetchData()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        release()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2f

        //无歌词
        if (!hasLyric()) {
            lyricPaint.color = currentTextColor
            val layout = DynamicLayout.Builder.obtain(NO_LYRIC_HINT, lyricPaint, width)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .build()
            drawText(canvas, layout, centerY)
            return
        }
        val centerLine = getCenterLine()

        if (isShowTimeline) {
            timelinePlayDrawable?.draw(canvas)
            timelinePaint.color = timelineColor
            canvas.drawLine(timelineTimeWidth, centerY, width - timelineTimeWidth, centerY, timelinePaint)

            timelinePaint.color = timelineTimeColor
            val timeText = formatTime(lyricDataList[centerLine].timeStamp)
            //因为textAlign=CENTER， drawText的x轴位置指的是文字中间的位置（http://www.gimoo.net/t/1605/5736f1f9af7a5.html）
            val timeX = width - timelineTimeWidth / 2f
            val timeY = centerY - (timelineFontMetrics.descent + timelineFontMetrics.ascent) / 2
            canvas.drawText(timeText, timeX, timeY, timelinePaint)
        }

        canvas.translate(0f, offset)

        var y = 0f
        lyricDataList.forEachIndexed { index, _ ->
            if (isShowTimeline && index == centerLine) {
                //拖动歌词时不要影响当前选中的歌词
                if (index != currentShowLine) {
                    lyricPaint.textSize = normalTextSize
                    lyricPaint.color = selectedTextColor
                } else {
                    lyricPaint.textSize = currentTextSize
                    lyricPaint.color = currentTextColor
                }
            } else {
                val progress = lyricDataList[index].highlightProgress
                 // 字体大小插值
                val size = normalTextSize + (currentTextSize - normalTextSize) * progress
                lyricPaint.textSize = size
                // 颜色插值
                lyricPaint.color = evaluateColor(
                    progress,
                    normalTextColor,
                    currentTextColor
                )
            }

            if (index > 0) {
                y += (lyricDataList[index - 1].getHeight() + lyricDataList[index].getHeight() / 2) + dividerHeight
            }

            if (lyricDataList[index].staticLayout != null) {
                drawText(canvas, lyricDataList[index].staticLayout!!, y)
            }

        }
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            offset = scroller.currY.toFloat()
            invalidate()
        }
        if (isFling && scroller.isFinished) {
            isFling = false
            if (hasLyric() && !isTouching) {
                adjustCenter()
                postDelayed(hideTimelineRunnable, TIMELINE_KEEP_TIME)
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~!!!以下是对外暴露的方法!!!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    fun setOnTapListener(l: OnTapListener) {
        onTapListener = l
    }

    fun setOnPlayClickListener(l: OnPlayClickListener) {
        onPlayClickListener = l
    }

    /**
     * 设置字体
     */
    fun setLyricFont(typeface: Typeface) {
        lyricPaint.typeface = typeface
    }

    /**
     * 设置歌词字体大小
     */
    fun setLyricTextSize(lyricTextSize: Float) {
        normalTextSize = lyricTextSize
    }

    /**
     * 设置播放歌词放大比例
     */
    fun setLyricHighLightTextZoom(rate: Float) {
        currentTextSize = normalTextSize * rate
    }

    /**
     * 设置默认歌词字体颜色
     */
    fun setLyricNormalTextColor(color: Int) {
        normalTextColor = color
        invalidate()
    }

    /**
     * 设置播放行的歌词字体颜色
     */
    fun setLyricHighLightTextColor(color: Int) {
        currentTextColor = color
        invalidate()
    }

    /**
     * 设置滑动歌词时，中间歌词行的颜色
     */
    fun setLyricSelectedTextColor(color: Int) {
        selectedTextColor = color
        invalidate()
    }

    /**
     * 设置时间线和时间线上的时间字体颜色
     */
    fun setTimelineColor(color: Int) {
        timelineColor = color
        timelineTimeColor = color
        invalidate()
    }

    /**
     * 设置时间线上按钮
     */
    fun setTimelinePlayDrawable(drawable: Drawable) {
        timelinePlayDrawable = drawable
        invalidate()
    }

    /**
     * 是否显示时间线
     */
    fun setShowTimeline(isShowTimeline: Boolean) {
        this.isShowTimeline = isShowTimeline
    }

    /**
     * 设置歌词阴影
     */
    fun setLyricShadow(
        radius: Float,
        dx: Float,
        dy: Float,
        color: Int
    ) {
        lyricShadowRadius = radius
        lyricShadowDx = dx
        lyricShadowDy = dy
        lyricShadowColor = color
        updateLyricShadow()
        invalidate()
    }

    /**
     * 页面恢复时调用
     */
    fun lifecycleResume() {
        if (isLifecyclePause) {
            isLifecyclePause = false
            updateTime(GlobalPlayManager.getPlayProgress())
        }
    }

    /**
     * 页面不可见时调用，避免资源浪费
     */
    fun lifecyclePause() {
        isLifecyclePause = true
    }

    private var currentTimestamp = -1L
    fun updateTime(timestamp: Long) {
        if (height == 0) {
            return
        }
        if (isLifecyclePause) {
            return
        }
        if (timestamp == currentTimestamp) {
            return
        }
        currentTimestamp = timestamp
        if (!hasLyric()) {
            return
        }

        val line = findShowLine(timestamp)
        if (line == 0) {
            smoothScrollToLine(currentShowLine)
            return
        }
        if (line != currentShowLine) {
            val oldLine = currentShowLine
            currentShowLine = line

            //开启高亮动画
            startHighlightAnimation(oldLine, currentShowLine)

            if (!isShowTimeline) {
                smoothScrollToLine(currentShowLine)
            } else {
                invalidate()
            }
        }
    }

    //更新歌词阴影参数
    private fun updateLyricShadow() {
        lyricPaint.setShadowLayer(
            lyricShadowRadius,
            lyricShadowDx,
            lyricShadowDy,
            lyricShadowColor
        )
    }

    private fun drawText(canvas: Canvas, staticLayout: DynamicLayout, y: Float) {
        val lyricPadding = timelinePlayDrawableSize
        canvas.save()
        val dy = y - (staticLayout.height / 2)
        canvas.translate(lyricPadding, dy)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    /**
     * 二分查找算法，查找当前时间应该显示的行数（最后一个 <=timestamp 的行数）
     */
    private fun findShowLine(timestamp: Long): Int {
        var left = 0
        var right = lyricDataList.size
        while (left <= right) {
            val middle = (left + right) / 2
            val middleTime = lyricDataList[middle].timeStamp

            if (timestamp < middleTime) {
                right = middle - 1
            } else {
                if (middle + 1 >= lyricDataList.size || timestamp <= lyricDataList[middle + 1].timeStamp) {
                    return middle
                }
                left = middle + 1
            }
        }
        return 0
    }

    /**
     * 根据当前歌词移动的距离：offset，来计算视图中间所对应的歌词行数
     * 使用二分查找
     * 结合这张图理解：https://www.processon.com/view/link/650cf5021245101a8651be43
     */
    private fun getCenterLine(): Int {
        if (lyricDataList.isEmpty()) return 0

        var left = 0
        var right = lyricDataList.lastIndex

        while (left <= right) {
            val mid = (left + right) ushr 1
            val midOffset = getLineOffset(mid)

            if (offset > midOffset) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        }

        val index = left.coerceIn(0, lyricDataList.lastIndex)

        if (index == 0) return 0
        if (index >= lyricDataList.size) return lyricDataList.lastIndex

        val prev = index - 1

        return if (
            abs(offset - getLineOffset(prev)) <
            abs(offset - getLineOffset(index))
        ) prev else index
    }

    /**
     * 将第line行移动到屏幕中间需要移动的距离
     */
    private fun getLineOffset(line: Int): Float {
        if (lyricDataList[line].lineOffset == Float.MIN_VALUE) {
            var offset = height / 2f
            for (i in 1 .. line) {
                offset -= lyricDataList[i - 1].getHeight() + lyricDataList[i].getHeight() / 2 + dividerHeight
            }
            lyricDataList[line].lineOffset = offset
        }
        return lyricDataList[line].lineOffset
    }

    /**
     * 首次进入后要快速定位到播放行
     */
    private fun quickScrollToLine(timestamp: Long) {
        if (isLifecyclePause) {
            return
        }
        val line = findShowLine(timestamp)

        currentShowLine = line
        //开启高亮动画
        lyricDataList[line].highlightProgress = 1f

        val lineOffset = getLineOffset(line)
        offset = lineOffset
        invalidate()
    }

    /**
     * 将中心行微调至正中心
     */
    private fun adjustCenter() {
        smoothScrollTo(getCenterLine(), ADJUST_DURATION)
    }

    /**
     * 滚动到某一行
     */
    private fun smoothScrollToLine(line: Int) {
        smoothScrollTo(line, SCROLL_DURATION)
    }

    /**
     * 滚动到某一行
     */
    private fun smoothScrollTo(line: Int, durationMills: Long) {
        val lineOffset = getLineOffset(line)
        stopScrollAnimation()
        scrollAnimator = ValueAnimator.ofFloat(offset, lineOffset).apply {
            duration = durationMills
            interpolator = LinearInterpolator()
            addUpdateListener {anim ->
                offset = anim.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun stopScrollAnimation() {
        scrollAnimator?.cancel()
    }

    private fun stopHighlightAnimator() {
        highlightAnimator?.cancel()
    }

    private fun listenData() {
        fetchLyricJob = SmartScope.launchUI {
            launch {
                LyricManager.lyricStateFlow.collect { state ->
                    when {
                        state is LyricLoadState.PREPARE -> {
                            reset()
                        }
                        state is LyricLoadState.ERROR -> {
                            reset()
                        }
                        state is LyricLoadState.SUCCESS -> {
                            if (state.originData.songId == GlobalPlayManager.getCurrentMusicInfo()?.id) {
                                reset()
                                //处理歌词
                                if (state.originData.noLyric) {
                                    return@collect
                                }
                                val lyricLineList = state.lyricLineList
                                lyricLineList?.map { lyricLine ->
                                    LyricData(timeStamp = lyricLine.timeStamp, lyricTxt = lyricLine.lyricTxt)
                                }?.run {
                                    lyricDataList.addAll(this)
                                    initLyricDataList()
                                }
                            }
                        }
                    }
                }
            }

//            launch {
//                PlayerFlows.switchMusicStateFlow.collect { musicInfo ->
//                    if (musicInfo != null) {
//                        //获取歌词
//                        LyricManager.fetchLyric(musicInfo.id)
//                    }
//                }
//            }

            launch {
                PlayerFlows.progressStateFlow.collect { progressState ->
                    val currentPlayTime = progressState.currentPlayTime
                    updateTime(currentPlayTime)
                }
            }
        }
    }

    private fun formatTime(milli: Int): String {
        val m = (milli / DateUtils.MINUTE_IN_MILLIS).toInt()
        val s = (milli / DateUtils.SECOND_IN_MILLIS % 60).toInt()
        val mm = String.format(Locale.getDefault(), "%02d", m)
        val ss = String.format(Locale.getDefault(), "%02d", s)
        return "$mm:$ss"
    }

    private fun hasLyric(): Boolean {
        return lyricDataList.isNotEmpty()
    }

    private fun fetchData() {
        val songId = GlobalPlayManager.getCurrentMusicInfo()?.id?.also {
            LyricManager.fetchLyric(it)
        }
        SDKLogUtil.i("[LyricView][fetchData] songId=$songId, songName=${GlobalPlayManager.getCurrentMusicInfo()?.name}")
    }

    /**
     * 布局完成后要重新设置下数据
     */
    private fun initLyricDataList() {
        if (width == 0) {
            doOnLayout {
                offset = height / 2f
                initLyricDataList()
            }
            return
        }
        lyricDataList.forEach {
            lyricPaint.textSize = currentTextSize
            val dynamicLayout = DynamicLayout.Builder.obtain(it.lyricTxt, lyricPaint, width - timelinePlayDrawableSize.toInt() * 2)
                .setAlignment(Layout.Alignment.ALIGN_CENTER).build()
            it.staticLayout = dynamicLayout
        }
        invalidate()

        if (isFirstInit) {
            isFirstInit = false
            initTimelinePlayDrawableSize()
            quickScrollToLine(GlobalPlayManager.getPlayProgress())
        }
    }

    private fun initTimelinePlayDrawableSize() {
        val l = (timelineTimeWidth - timelinePlayDrawableSize) / 2
        val t = height / 2 - timelinePlayDrawableSize / 2
        val r = l + timelinePlayDrawableSize
        val b = t + timelinePlayDrawableSize
        timelinePlayDrawable?.setBounds(l.toInt(), t.toInt(), r.toInt(), b.toInt())
    }

    private fun reset() {
        stopScrollAnimation()
        stopHighlightAnimator()
        scroller.forceFinished(true)
        isShowTimeline = false
        isTouching = false
        isFling = false
        removeCallbacks(hideTimelineRunnable)
        lyricDataList.clear()
        offset = height / 2f
        currentShowLine = 0
        currentTimestamp = -1
        invalidate()
        SDKLogUtil.i("[LyricView][reset]")
    }

    /**
     * 控件销毁时释放资源
     */
    private fun release() {
        fetchLyricJob?.cancel()
        fetchLyricJob = null
        scroller.forceFinished(true)
        removeCallbacks(hideTimelineRunnable)
        stopScrollAnimation()
        stopHighlightAnimator()
    }

    /**
     * 颜色插值
     */
    private fun evaluateColor(fraction: Float, startColor: Int, endColor: Int): Int {
        val startA = Color.alpha(startColor)
        val startR = Color.red(startColor)
        val startG = Color.green(startColor)
        val startB = Color.blue(startColor)

        val endA = Color.alpha(endColor)
        val endR = Color.red(endColor)
        val endG = Color.green(endColor)
        val endB = Color.blue(endColor)

        val a = (startA + (fraction * (endA - startA))).toInt()
        val r = (startR + (fraction * (endR - startR))).toInt()
        val g = (startG + (fraction * (endG - startG))).toInt()
        val b = (startB + (fraction * (endB - startB))).toInt()

        return Color.argb(a, r, g, b)
    }

    /**
     * 高亮动画
     */
    private fun startHighlightAnimation(oldLine: Int, newLine: Int) {
        highlightAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = SCROLL_DURATION
            addUpdateListener { anim ->
                if (hasLyric()) {
                    val progress = anim.animatedValue as Float

                    // 新行 0 -> 1
                    lyricDataList[newLine].highlightProgress = progress

                    // 旧行 1 -> 0
                    if (oldLine >= 0 && oldLine < lyricDataList.size) {
                        lyricDataList[oldLine].highlightProgress = 1f - progress
                    }

                    invalidate()
                }
            }
            start()
        }
    }

    private val gestureListener = object: GestureDetector.SimpleOnGestureListener() {

        // 本次点击仅仅为了停止歌词滚动，则不响应点击事件
        private var isTouchForStopFling = false

        override fun onDown(e: MotionEvent): Boolean {

            if (!hasLyric()) {
                return onTapListener != null
            }
            isTouching = true
            removeCallbacks(hideTimelineRunnable)
            if (isFling) {
                isTouchForStopFling = true
                scroller.forceFinished(true)
            } else {
                isTouchForStopFling = false
            }

            return onPlayClickListener != null || onTapListener != null
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!hasLyric() || onPlayClickListener == null) {
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            if (!isShowTimeline) {
                isShowTimeline = true
            } else {
                offset += -distanceY
                offset = min(offset, getLineOffset(0))
                offset = max(offset, getLineOffset(lyricDataList.size - 1))
            }

            invalidate()

            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (!hasLyric() || onPlayClickListener == null) {
                return super.onFling(e1, e2, velocityX, velocityY)
            }
            if (isShowTimeline) {
                isFling = true
                removeCallbacks(hideTimelineRunnable)
                scroller.fling(
                    0,
                    offset.toInt(),
                    0,
                    velocityY.toInt(),
                    0,
                    0,
                    getLineOffset(lyricDataList.size - 1).toInt(),
                    getLineOffset(0).toInt()
                )
                return true
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val isPlayDrawableClick: Boolean = timelinePlayDrawable?.bounds?.contains(e.x.toInt(), e.y.toInt()) == true
            if (hasLyric() && onPlayClickListener != null && isShowTimeline && isPlayDrawableClick) {
                val centerLine = getCenterLine()
                val centerLineTime = lyricDataList[centerLine].timeStamp
                // onPlayClick 消费了才更新 UI
                if (onPlayClickListener?.invoke(this@LyricView, centerLineTime) == true) {
                    isShowTimeline = false
                    removeCallbacks(hideTimelineRunnable)
//                    currentShowLine = centerLine
                    invalidate()
                    return true
                }
            } else if (!isTouchForStopFling) {
                onTapListener?.invoke(this@LyricView, e.x, e.y)
            }
            return super.onSingleTapConfirmed(e)
        }
    }

}