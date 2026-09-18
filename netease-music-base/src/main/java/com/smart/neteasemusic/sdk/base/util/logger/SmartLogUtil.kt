package com.smart.neteasemusic.sdk.base.util.logger

import timber.log.Timber

/**
 * @author david.long 2024/7/9 19:19
 */
object SmartLogUtil {

    // 默认标签
    private var currentTag: String = "SmartLogUtil"

    @JvmStatic
    fun init(tag: String) {
        currentTag = tag
        plant()
    }

    fun plant(tree: Timber.Tree = Timber.DebugTree()) {
        if (Timber.forest().isEmpty()) {
            Timber.plant(tree)
        } else {
            Timber.w("Timber already initialized, skipping.")
        }
    }

    /**
     * 打印带线程信息的日志
     */
    fun plantThreadTree() {
        Timber.uprootAll()
        plant(ThreadLoggingTree())
    }

    fun plantDebugTree() {
        Timber.uprootAll()
        plant()
    }

    @JvmStatic
    fun v(tag: String = currentTag, msg: String?) {
        Timber.tag(tag).v(msg)
    }

    @JvmStatic
    fun d(tag: String = currentTag, msg: String?) {
        Timber.tag(tag).d(msg)
    }

    @JvmStatic
    fun i(tag: String = currentTag, msg: String?) {
        Timber.tag(tag).i(msg)
    }

    @JvmStatic
    fun w(tag: String = currentTag, msg: String?) {
        Timber.tag(tag).w(msg)
    }

    @JvmStatic
    fun e(tag: String = currentTag, msg: String? = null, throwable: Throwable? = null) {
        Timber.tag(tag).e(throwable, msg)
    }
}