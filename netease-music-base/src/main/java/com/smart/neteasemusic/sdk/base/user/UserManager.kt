package com.smart.neteasemusic.sdk.base.user

import androidx.annotation.Keep
import com.smart.neteasemusic.sdk.base.bean.AccessToken
import com.smart.neteasemusic.sdk.base.bean.Profile
import com.smart.neteasemusic.sdk.base.common.VipType
import com.smart.neteasemusic.sdk.base.login.LoginManager
import com.smart.neteasemusic.sdk.base.login.LoginState
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.base.util.fromJson
import com.smart.neteasemusic.sdk.base.util.toJson
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 用户信息管理
 * @author david.long 2026/1/12 09:11
 */
object UserManager {

    private const val KEY_PROFILE = "key_profile"
    private const val KEY_MY_LIKE_PLAYLIST_ID = "key_my_like_playlist_id"
    private val list = CopyOnWriteArrayList<ProfileChangeListener>()

    var profile: Profile? = SpStorage.getString(KEY_PROFILE)?.fromJson()

    // 提前10分钟刷新token，避免边界情况
    private const val REFRESH_AHEAD_TIME = 10 * 60 * 1000L

    fun getUserId(): String? {
        return profile?.id
    }

    fun isLogin(): Boolean {
        return LoginManager.isLogin()
    }

    /**
     * 外部调用请使用 [GlobalPlayManager.logout()]
     */
    fun logout() {
        profile = null
        LoginManager.logout()
    }

        fun isBlackVip(): Boolean {
        return isVipTypeVip(VipType.BLACK_VIP)
    }

    fun isSVip(): Boolean {
        return isVipTypeVip(VipType.S_VIP)
    }

    fun isCarVip(): Boolean {
        return isVipTypeVip(VipType.IOT_CAR_VIP)
    }

    fun isVip(): Boolean {
        return isCarVip() || isBlackVip() || isSVip()
    }

    /**
     * 刷新用户信息
     */
    suspend fun refreshProfile() {
        LoginManager.fetchProfile()
    }

    /**
     * 内部调用方法
     */
    fun updateProfile(profile: Profile?) {
        SDKLogUtil.i("[UserManager][updateProfile] profile=$profile")
        this.profile = profile
        if (null == profile) {
            SpStorage.remove(KEY_PROFILE)
        } else {
            SpStorage.putString(KEY_PROFILE, profile.toJson())
        }
        notifyProfileUpdate(profile)
    }

    /**
     * 添加用户信息变更监听器
     */
    fun addProfileChangeListener(listener: ProfileChangeListener) {
        list.forEach {
            if (it == listener) {
                return
            }
        }
        list.add(listener)
    }

    /**
     * 移除用户信息变更监听器
     */
    fun removeProfileChangeListener(listener: ProfileChangeListener) {
        list.forEach {
            if (it == listener) {
                list.remove(it)
            }
        }
    }

    private fun notifyProfileUpdate(profile: Profile?) {
        list.forEach { listener ->
            try {
                listener.onChanged(profile)
            } catch (e: Exception) {
                SDKLogUtil.e("[RetryInterceptor][notifyProfileUpdate] Notify profile change failed", e)
            }
        }
    }

    private fun isVipTypeVip(vipType: Int): Boolean {
        if (profile?.vipDetail.isNullOrEmpty()) {
            return false
        }
        val vipInfo = profile?.vipDetail?.firstOrNull() {
            it.type == vipType
        }
        return if (vipInfo != null) {
            val expireTime = vipInfo.expireTime
            val currentTime = System.currentTimeMillis()
            expireTime > currentTime + REFRESH_AHEAD_TIME
        } else {
            false
        }
    }

    /**
     * 保存我喜欢的 歌单id
     */
    fun saveMyLikePlaylistId(playlistId: String) {
        SpStorage.putString(KEY_MY_LIKE_PLAYLIST_ID, playlistId)
    }

    /**
     * 获取我喜欢的 歌单id
     */
    fun getMyLikePlaylistId(): String? {
        return SpStorage.getString(KEY_MY_LIKE_PLAYLIST_ID, null)
    }

    suspend fun authenticatedLogin(accessToken: AccessToken) {
        LoginManager.authenticatedLogin(accessToken)
    }

    fun getCurrentLoginState(): LoginState {
        return LoginManager.getCurrentLoginState()
    }

    fun getToken(): String? {
        val loginState = getCurrentLoginState()
        return if (loginState is LoginState.Authenticated) {
            loginState.token
        } else {
            null
        }
    }
}

/**
 * 用户信息变更监听器
 */
@Keep
fun interface ProfileChangeListener {
    /**
     * 当用户信息发生变化时调用
     * @param profile 新的用户信息，null 表示用户信息被清除
     */
    fun onChanged(profile: Profile?)
}