package com.smart.neteasemusic.sdk.base.storage

import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import androidx.core.content.edit

/**
 * 保存一些通用信息
 * @author david.long 2024/12/3 12:10
 */
object SpStorage {

    private const val PREFERENCE_NAME: String = "com.smart.dhu.netease.preferences.default"

    fun init(context: Context) {
        MMKV.initialize(context)
        MMKV.setLogLevel(MMKVLogLevel.LevelNone)
    }

    fun getInt(key: String, defValue: Int): Int {
        return getMainPreference().getInt(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        getMainPreference().edit { putInt(key, value) }
    }

    fun getString(key: String, defValue: String? = null): String? {
        return getMainPreference().getString(key, defValue)
    }

    fun putString(key: String, value: String?) {
        getMainPreference().edit { putString(key, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        getMainPreference().edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return getMainPreference().getBoolean(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        getMainPreference().edit { putLong(key, value) }
    }

    fun getLong(key: String, defValue: Long): Long {
        return getMainPreference().getLong(key, defValue)
    }

    fun putFloat(key: String, value: Float) {
        getMainPreference().edit { putFloat(key, value) }
    }

    fun getFloat(key: String, defValue: Float): Float {
        return getMainPreference().getFloat(key, defValue)
    }

    fun remove(key: String) {
        return getMainPreference().edit { remove(key) }
    }

    fun clear(defName: String = PREFERENCE_NAME) {
        getMainPreference(defName).edit { clear() }
    }

    fun getMainPreference(defName: String = PREFERENCE_NAME): SharedPreferences {
        return MMKV.mmkvWithID(defName)
    }

}