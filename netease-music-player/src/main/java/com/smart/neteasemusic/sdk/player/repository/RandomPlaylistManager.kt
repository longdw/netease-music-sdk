package com.smart.neteasemusic.sdk.player.repository

import com.smart.neteasemusic.sdk.player.PlayerProxy

/**
 * @author david.long 2026/1/22 18:34
 */
internal class RandomPlaylistManager : BasePlaylistManager() {

    override fun getCurrentPlayIndex(): Int {

        val currentPlayMusicInfo = PlayerProxy.getCurrentMusicInfo()
        val findIndex = playlist.indexOfFirst {
            it.id == currentPlayMusicInfo?.id
        }

        return findIndex
    }

}