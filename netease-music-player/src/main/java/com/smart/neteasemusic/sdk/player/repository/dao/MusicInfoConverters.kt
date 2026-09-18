package com.smart.neteasemusic.sdk.player.repository.dao

import androidx.room.TypeConverter
import com.smart.neteasemusic.sdk.base.bean.FreeTrail
import com.smart.neteasemusic.sdk.base.util.fromJson
import com.smart.neteasemusic.sdk.base.util.toJson
import com.smart.neteasemusic.sdk.data.bean.VoiceDTO

/**
 * @author david.long 2026/2/9 11:09
 */
internal class MusicInfoConverters {

    @TypeConverter
    fun fromFreeTrial(value: FreeTrail?): String? {
        return value?.toJson()
    }

    @TypeConverter
    fun toFreeTrial(value: String?): FreeTrail? {
        return value?.fromJson<FreeTrail>()
    }

    @TypeConverter
    fun fromVoiceDTO(value: VoiceDTO?): String? {
        return value?.toJson()
    }

    @TypeConverter
    fun toVoiceDTO(value: String?): VoiceDTO? {
        return value?.fromJson<VoiceDTO>()
    }

}