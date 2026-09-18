package com.smart.neteasemusic.sdk.data.base

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.network.ERROR
import com.smart.neteasemusic.sdk.base.network.SUCCESS
import com.smart.neteasemusic.sdk.base.network.State
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author david.long 2026/1/14 08:58
 */
open class BaseApiImpl {

    protected suspend fun <T> runOnUI(block: () -> State<T>) =
        withContext(Dispatchers.Main.immediate) {
            block()
        }

    protected suspend fun <T> success(state: T): State<T> {
        return runOnUI {
            SUCCESS(state)
        }
    }

    protected suspend fun <T> error(error: Throwable?, code: Int? = null, msg: String? = null): State<T> {
        return runOnUI {
            ERROR(error, message = msg, code = code)
        }
    }

    protected suspend fun <T> startFetch(block: suspend () -> ApiResult<T>): State<T> {
        return runCatching {
            val result = block()
            val data = result.data
            SDKLogUtil.i("[BaseApiImpl] isSuccess=${result.isSuccess()} code=${result.code}")
            if (result.isSuccess() && data != null) {
                success(data)
            } else {
                error(result.exception, result.code, result.message)
            }
        }.getOrElse {
            SDKLogUtil.e("[BaseApiImpl] ${it.message}", it)
            error(it)
        }
    }

}