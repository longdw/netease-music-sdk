package com.smart.neteasemusic.sdk.player.repository

import com.smart.neteasemusic.sdk.base.util.SDKLogUtil
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.GlobalPlayManager
import com.smart.neteasemusic.sdk.player.PlayerProxy

/**
 * 管理当前正在播放的歌曲列表
 * @author david.long 2026/1/20 13:42
 */
internal open class BasePlaylistManager {

    companion object {
        const val PLAY_LIST_ID_DEFAULT = 1
        const val PLAY_LIST_ID_RANDOM = 2
    }

    private val _playlist = mutableListOf<MusicInfo>()
    val playlist: List<MusicInfo>
        get() {
            return _playlist.toList()
        }

    open fun updatePlaylist(list: List<MusicInfo>) {
        _playlist.clear()
        _playlist.addAll(list)
    }

    /**
     * @return -1   表示当前正在播放的歌曲下一首就是要插入的歌曲，这样就无需再插入了
     *         非-1 插入成功，返回的值就是插入的位置
     */
    open fun insertIntoPlaylist(musicInfo: MusicInfo): Int {
        val findIndex = playlist.indexOfFirst {
            it.id == musicInfo.id
        }
        val currentPlayIndex = getCurrentPlayIndex()
        SDKLogUtil.i("[BasePlaylistManager][insertIntoPlaylist] 添加到下一首$this, currentPlayIndex=$currentPlayIndex")
        val nextIndex = currentPlayIndex + 1
        //如果播放列表中找到了待插入的歌曲
        if (findIndex != -1) {
            //如果当前播放的下一首就是待插入的歌曲
            return if (nextIndex == findIndex) {
                findIndex
            } else {
                //移除并重新插入到当前播放的下一首
                val newPlayList = playlist.toMutableList()
                val findResult = newPlayList.firstOrNull {
                    musicInfo.id == it.id
                }
                if (findResult != null) {
                    newPlayList.remove(findResult)
                }

                //防止remove后索引越界
                val insertIndex = nextIndex.coerceAtMost(newPlayList.size)
                newPlayList.add(insertIndex, musicInfo)
                updatePlaylist(newPlayList)
                insertIndex
            }
        }

        //插入到当前播放的下一首
        val newPlayList = playlist.toMutableList()
        //防止currentPlayIndex失效
        val insertIndex = nextIndex.coerceAtMost(newPlayList.size)
        newPlayList.add(insertIndex, musicInfo)
        updatePlaylist(newPlayList)

        SDKLogUtil.i("[PlaylistManager][insertIntoPlaylist] 添加到下一首$this, 插入的位置：$insertIndex")
        return insertIndex
    }

    open fun insertIntoPlaylist(musicList: List<MusicInfo>) {
        val originMusicList = _playlist.toMutableList()
        val currentMusicInfo = GlobalPlayManager.getCurrentMusicInfo()

        for (musicInfo in musicList) {
            val findResult = _playlist.firstOrNull {
                musicInfo.id == it.id
            }
            if (findResult != null && findResult.id != currentMusicInfo?.id) {
                originMusicList.remove(findResult)
            }
        }

        originMusicList.addAll(musicList)
        updatePlaylist(originMusicList)
    }

    open fun getCurrentPlayIndex() = PlayerProxy.getCurrentPlayIndex()

    open fun removeFromPlaylist(musicInfo: MusicInfo) {
        val findResult = _playlist.firstOrNull {
            musicInfo.id == it.id
        }
        if (findResult != null) {
            _playlist.remove(findResult)
        }
    }

    fun clearPlaylist() {
        _playlist.clear()
    }
}