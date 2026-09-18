package com.smart.neteasemusic.business.play

import com.smart.neteasemusic.business.common.PlaySource
import com.smart.neteasemusic.business.common.ShortcutState
import com.smart.neteasemusic.sdk.base.storage.SpStorage
import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.player.GlobalPlayManager

/**
 * 快捷播放管理类
 * @author david.long 2026/2/17 20:12
 */
object ShortcutPlayManager {

    private const val KEY_PLAY_SOURCE_ID = "key_play_source_id"
    private const val KEY_PLAY_SOURCE = "key_play_source"

    /**
     * 播放来源的id - 如歌单的id，播客的id等
     */
    val playSourceId
        get() = if (_currentShortcutState == ShortcutState.PREPARED || _currentShortcutState == ShortcutState.LOADING) {
            tempPlaySourceId
        } else {
            _playSourceId
        }

    /**
     * 播放来源 - [com.smart.neteasemusic.business.common.PlaySource]
     */
    val playSource
        get() = _playSource

    /**
     * 播放状态 - [ShortcutState]
     */
    val shortcutState
        get() = _currentShortcutState

    private var _playSourceId: String?
        set(value) {
            SpStorage.putString(KEY_PLAY_SOURCE_ID, value)
        }
        get() = SpStorage.getString(KEY_PLAY_SOURCE_ID)

    private var _playSource: String?
        set(value) {
            value?.let {
                SpStorage.putString(KEY_PLAY_SOURCE, value)
            }
        }
        get() = SpStorage.getString(KEY_PLAY_SOURCE, PlaySource.OTHER)

    private var tempPlaySourceId: String? = null
    private var tempPlaySource: String? = null
    private var _currentShortcutState: ShortcutState = ShortcutState.NONE

    fun loading(playSourceId: String?, @PlaySource.PlaySourceType playSource: String) {
        tempPlaySourceId = playSourceId
        tempPlaySource = playSource
        _currentShortcutState = ShortcutState.LOADING
    }

    fun prepared() {
        _currentShortcutState = ShortcutState.PREPARED
    }

    fun playing() {
        if (_currentShortcutState == ShortcutState.PREPARED || _currentShortcutState == ShortcutState.LOADING) {
            _playSourceId = tempPlaySourceId
            _playSource = tempPlaySource
        }
        SDKLogUtil.i("[ShortcutPlayManager][playing] _currentShortcutState=$_currentShortcutState, _playSourceId=$_playSourceId, _playSource=$_playSource")
        _currentShortcutState = ShortcutState.PLAYING
    }

    fun playing(playSourceId: String?, @PlaySource.PlaySourceType playSource: String) {
        _playSourceId = playSourceId
        _playSource = playSource
        _currentShortcutState = ShortcutState.PLAYING
    }

    fun pause() {
        //因为切歌后会走到暂停
        //如果是准备状态，则不执行暂停，否则playing()方法无法正确赋值导致状态错乱
        if (_currentShortcutState == ShortcutState.PREPARED || _currentShortcutState == ShortcutState.LOADING) {
            return
        }
        _currentShortcutState = ShortcutState.PAUSE
    }

    fun error() {
        tempPlaySourceId = null
        tempPlaySource = null
        _currentShortcutState = if (GlobalPlayManager.isPlaying()) {
            ShortcutState.PLAYING
        } else if (GlobalPlayManager.isPause()) {
            ShortcutState.PAUSE
        } else {
            ShortcutState.NONE
        }
    }

    fun shouldPlay(playSourceId: String?) = tempPlaySourceId == playSourceId

    fun shouldRefresh(id: String?): Boolean {
        return id != null && (id == tempPlaySourceId || id == _playSourceId)
    }
}