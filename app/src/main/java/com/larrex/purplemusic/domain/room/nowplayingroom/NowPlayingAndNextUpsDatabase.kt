package com.larrex.purplemusic.domain.room.nowplayingroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NowPlaying::class, NextUpSongs::class], version = 1)
abstract class NowPlayingAndNextUpsDatabase : RoomDatabase() {

    abstract fun dao(): NowPlayingAndNextUpsDao

}