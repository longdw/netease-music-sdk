package com.smart.neteasemusic.sdk.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 艺人数据结构
 * Properties:
 * id - 艺人Id
 * name - 艺人名称
 * transName - 艺人别名
 * coverImgUrl - 艺人封面
 * type - 艺人类型（3:乐队组合, 1:男歌手, 2:女歌手）
 * authMusicianV - 大V认证
 * identity - 身份
 * roles - 艺人角色信息
 * subCount - 粉丝数
 * briefDesc - 简要描述
 * nationality - 国家/地区
 * @author david.long 2026/1/14 09:18
 */
@JsonClass(generateAdapter = true)
@Keep
data class ArtistDTO(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "transName") val transName: String? = "",
    @field:Json(name = "coverImgUrl") val coverImgUrl: String? = "",
    @field:Json(name = "type") val type: Int = 0,
    @field:Json(name = "authMusicianV") val authMusicianV: Boolean? = false,
    @field:Json(name = "identity") val identity: List<IdentityDTO>? = listOf(),
    @field:Json(name = "roles") val roles: List<ExpertIdentityTypeDTO>? = listOf(),
    @field:Json(name = "subCount") val subCount: Long = 0,
    @field:Json(name = "briefDesc") val briefDesc: String? = "",
    @field:Json(name = "nationality") val nationality: String? = ""
) {
    fun isTypeMaleSinger(): Boolean {
        return TYPE_MALE_SINGER == type
    }

    fun isTypeFemaleSinger(): Boolean {
        return TYPE_FEMALE_SINGER == type
    }

    fun isTypeBand(): Boolean {
        return TYPE_BAND == type
    }

    @Keep
    companion object {
        /** 男歌手 */
        const val TYPE_MALE_SINGER = 1

        /** 女歌手 */
        const val TYPE_FEMALE_SINGER = 2

        /** 乐队组合 */
        const val TYPE_BAND = 3
    }
}

data class ArtistResultDTO(
    val artists: List<ArtistDTO>?
)