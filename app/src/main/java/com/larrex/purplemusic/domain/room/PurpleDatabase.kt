package com.larrex.purplemusic.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NowPlaying::class, NextUpSongs::class, Playlist::class], version = 1)
abstract class PurpleDatabase : RoomDatabase() {

    abstract fun dao(): PurpleDao

}