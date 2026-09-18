package com.smart.neteasemusic.sdk.player.exo

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * 自定义的音频加载错误处理策略，是内部消化还是onPlayerError抛给上层处理
 * @author david.long 2026/3/24 10:10
 */
@OptIn(UnstableApi::class)
@Deprecated("不能加这个策略，否则跳帧策略就不生效，导致死循环报错")
class AudioLoadErrorHandlingPolicy : DefaultLoadErrorHandlingPolicy() {

    companion object {
        //最大底层重试次数
        private const val MAX_NETWORK_RETRY_COUNT = 2
        //重试间隔
        private const val RETRY_DELAY_MS = 200L
    }


    override fun getRetryDelayMsFor(loadErrorInfo: LoadErrorHandlingPolicy.LoadErrorInfo): Long {
        val exception = loadErrorInfo.exception
        val errorCount = loadErrorInfo.errorCount

        //处理网络闪断
        if (exception is HttpDataSource.HttpDataSourceException) {
            val cause = exception.cause
            if (cause is SocketException || cause is SocketTimeoutException || cause is ProtocolException) {
                return if (errorCount < MAX_NETWORK_RETRY_COUNT) {
                    SDKLogUtil.e("[AudioLoadErrorHandlingPolicy][getRetryDelayMsFor] 底层网络拦截: 检测到连接重置/超时，准备第 $errorCount 次隐式重试...")
                    //给予一个短暂的固定延迟，比如 200ms 后重试，给底层 Socket 释放留点时间
                    RETRY_DELAY_MS
                } else {
                    SDKLogUtil.e("[AudioLoadErrorHandlingPolicy][getRetryDelayMsFor] 底层网络重试超限 ($MAX_NETWORK_RETRY_COUNT 次)，准备抛出致命错误")
                    //返回 TIME_UNSET 表示不再重试，直接抛给 onPlayerError
                    C.TIME_UNSET
                }
            }
        }

        //处理 HTTP 致命错误（如 403, 416）
        //对于这类错误，重试是徒劳的，直接放弃底层重试，抛给上层的 onPlayerError 去换 URL
        if (exception is HttpDataSource.InvalidResponseCodeException) {
            val code = exception.responseCode
            if (code == 403 || code == 416) {
                SDKLogUtil.e("[AudioLoadErrorHandlingPolicy][getRetryDelayMsFor] 底层拦截: 遇到致命 HTTP 状态码 $code，放弃重试，直接上报")
                return C.TIME_UNSET
            }
        }

        //其他常规错误，交给 ExoPlayer 默认的策略（基于错误次数逐渐增加重试间隔）
        return super.getRetryDelayMsFor(loadErrorInfo)
    }
}