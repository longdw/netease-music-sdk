package com.smart.neteasemusic.sdk.player.bean

import androidx.room.Entity

/**
 * 因为随机播放列表和正常播放列表都要用到MusicInfo，但是MusicInfo表只有一个，所以新建了这个中间表
 * 播放列表和音乐的关系表
 * playlistId可以泛指播放队列id，更准确的说可以称为类型
 * @author david.long 2026/2/9 14:13
 */
@Entity(
    primaryKeys = [ "playlistId", "musicId" ]
)
internal data class PlaylistMusicCrossRef(
    val playlistId: Int,
    val musicId: String,
    val orderInList: Int
)
