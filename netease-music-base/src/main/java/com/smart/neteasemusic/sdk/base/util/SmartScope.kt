package com.smart.neteasemusic.sdk.base.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author david.long 2026/2/25 15:16
 */
object SmartScope : CoroutineScope {
    private val supervisorJob = SupervisorJob()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        SDKLogUtil.e("SmartScope==>launch failed", throwable)
    }

    override val coroutineContext: CoroutineContext =
        supervisorJob + exceptionHandler

    private val IOScope = CoroutineScope(supervisorJob + Dispatchers.IO + exceptionHandler)
    private val MainScope = CoroutineScope(supervisorJob + Dispatchers.Main.immediate + exceptionHandler)
    private val DefaultScope = CoroutineScope(supervisorJob + Dispatchers.Default + exceptionHandler)

    fun launchUI(block: suspend CoroutineScope.() -> Unit): Job = MainScope.launch(block = block)
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job = IOScope.launch(block = block)
    fun launchDefault(block: suspend CoroutineScope.() -> Unit): Job = DefaultScope.launch(block = block)

    fun cancel() = supervisorJob.cancel()
}