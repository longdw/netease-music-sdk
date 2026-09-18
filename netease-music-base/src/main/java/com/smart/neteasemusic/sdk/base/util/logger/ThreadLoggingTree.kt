package com.smart.neteasemusic.sdk.base.util.logger

import timber.log.Timber

/**
 * @author david.long 2024/9/26 11:28
 */
class ThreadLoggingTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        val threadName = Thread.currentThread().name
        val newTag = "$tag-$threadName"

        super.log(priority, newTag, message, t)
    }
}