package com.smart.neteasemusic.sdk.player.repository

import com.smart.neteasemusic.sdk.data.bean.MusicInfo

/**
 * @author david.long 2026/2/9 13:26
 */
internal class DefaultPlaylistManager : BasePlaylistManager() {

    override fun updatePlaylist(list: List<MusicInfo>) {
        super.updatePlaylist(list)

        //数据库存储列表
        DB.playlistDao().insertData(
            PLAY_LIST_ID_DEFAULT,
            list
        )
    }

}