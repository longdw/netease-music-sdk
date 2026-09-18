package com.smart.neteasemusic.sdk.player.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.bean.PlaylistMusicCrossRef

/**
 * @author david.long 2026/2/9 10:37
 */
@Dao
@TypeConverters(MusicInfoConverters::class)
internal interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusicList(list: List<MusicInfo>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPlaylistRef(playlistRef: PlaylistRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMusicCrossRef(list: List<PlaylistMusicCrossRef>)

    @Query("DELETE FROM PlaylistMusicCrossRef WHERE playlistId=:playlistId")
    fun clearPlaylistMusicCrossRef(playlistId: Int)

    @Query("DELETE FROM PlaylistMusicCrossRef")
    fun clearPlaylistMusicCrossRef()

    @Query("DELETE FROM MusicInfo")
    fun clearMusicInfo()

    @Transaction
    fun clear() {
        clearPlaylistMusicCrossRef()
        clearMusicInfo()
    }

    @Transaction
    fun insertData(
        playlistId: Int,
        musicList: List<MusicInfo>,
    ) {
        insertMusicList(musicList)
//        insertPlaylistRef(playlistRef)
        //清除关系表
        clearPlaylistMusicCrossRef(playlistId)

        //重建关系
        val refs = musicList.mapIndexed { index, music ->
            PlaylistMusicCrossRef(
                playlistId,
                music.id,
                index
            )
        }
        insertPlaylistMusicCrossRef(refs)
    }

//    @Deprecated("暂时废弃不用")
//    @Transaction
//    @Query("SELECT * FROM PlaylistRef WHERE playlistId=:playlistId")
//    fun getPlaylistWithMusic(playlistId: Int): PlaylistWithMusic?

    @Query("""
        SELECT m.* 
        FROM MusicInfo m
        JOIN PlaylistMusicCrossRef r
        ON m.id = r.musicId
        WHERE r.playlistId = :playlistId
        ORDER BY r.orderInList ASC
    """)
    fun getMusicListByPlaylist(playlistId: Int): List<MusicInfo>?
}