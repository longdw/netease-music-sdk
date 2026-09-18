package com.smart.neteasemusic.sdk.data.bean

data class MusicCriticismDTO(
    val image: String,// 封面
    val background: String,// 背景
    val contents: List<String>,// 评论列表
    val subContent: String,// 评论作者
    val icon: String, // 乐评logo
    val songListVoList: List<MusicCriticismSongDTO>, // 歌曲信息
)

data class MusicCriticismSongDTO(
    val name: String, // 歌曲名称
    val albumName: String?,// 专辑名
    val albumArtistName: String?, // 专辑艺人名
    val artistName: String?,// 艺人名
    val coverImgUrl: String?,
)