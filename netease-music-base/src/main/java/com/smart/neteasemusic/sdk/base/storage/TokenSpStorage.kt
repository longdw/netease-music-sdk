package com.smart.neteasemusic.sdk.base.storage

import androidx.core.content.edit
import com.smart.neteasemusic.sdk.base.storage.SpStorage.getMainPreference

/**
 * @author david.long 2026/3/3 18:52
 */
internal object TokenSpStorage {

    private const val PREFERENCE_NAME: String = "com.smart.dhu.netease.preferences.token"

    fun getString(key: String, defValue: String? = null): String? {
        return getMainPreference(PREFERENCE_NAME).getString(key, defValue)
    }

    fun putString(key: String, value: String?) {
        getMainPreference(PREFERENCE_NAME).edit { putString(key, value) }
    }

    fun putLong(key: String, value: Long) {
        getMainPreference(PREFERENCE_NAME).edit { putLong(key, value) }
    }

    fun getLong(key: String, defValue: Long): Long {
        return getMainPreference(PREFERENCE_NAME).getLong(key, defValue)
    }

    fun remove(key: String) {
        return getMainPreference(PREFERENCE_NAME).edit { remove(key) }
    }
}