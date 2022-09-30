package com.larrex.purplemusic.domain.room.nowplayingroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NowPlayingAndNextUpsDao {

    @Insert
    fun insertNowPlaying(nowPlaying: NowPlaying)

    @Insert
    fun insertNextUps(nextUpSongs: NextUpSongs)

    @Query("SELECT * FROM NowPlaying")
    fun getNowPlaying() : NowPlaying

    @Query("SELECT * FROM NextUpSongs")
    fun getNextUps() : List<NextUpSongs>

    @Query("DELETE FROM NowPlaying")
    fun deleteNowPlay()

    @Query("DELETE FROM NextUpSongs")
    fun deleteNextUps()


}