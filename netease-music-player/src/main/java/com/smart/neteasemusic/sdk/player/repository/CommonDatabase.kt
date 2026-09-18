package com.smart.neteasemusic.sdk.player.repository

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smart.neteasemusic.sdk.base.SDKContext
import com.smart.neteasemusic.sdk.data.bean.MusicInfo
import com.smart.neteasemusic.sdk.player.bean.PlaylistMusicCrossRef
import com.smart.neteasemusic.sdk.player.repository.dao.MusicInfoConverters
import com.smart.neteasemusic.sdk.player.repository.dao.PlaylistDao

/**
 * @author david.long 2026/2/9 09:58
 * 开发阶段这个version号不要修改，如果涉及新增表或者修改表结构，直接删除旧的apk，重新安装。后续等release集成的时候，如果再有变动，再修改version号
 */
@Database(
    entities = [MusicInfo::class, PlaylistMusicCrossRef::class],
    version = 1
)
@TypeConverters(MusicInfoConverters::class)
internal abstract class CommonDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}

private const val DB_NAME = "neteasemusic.db"
internal val DB: CommonDatabase by lazy {
    Room.databaseBuilder(SDKContext.appContext, CommonDatabase::class.java, DB_NAME)
        .allowMainThreadQueries()
        .build()
}