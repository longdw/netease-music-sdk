package com.smart.neteasemusic.sdk.base.flow

import com.smart.neteasemusic.sdk.base.login.LoginManager
import com.smart.neteasemusic.sdk.base.login.TokenManager
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author david.long 2026/3/4 10:11
 */
/**
 * token过期后通知
 */
val authSharedFlow = TokenManager.authExpiredSharedFlow.asSharedFlow()

/**
 * 登录状态
 */
val loginStateFlow = LoginManager.loginStateFlow.asStateFlow()

/**
 * 用户信息更新
 */
val profileStateFlow = LoginManager.profileStateFlow.asStateFlow()
