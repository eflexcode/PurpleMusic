package com.larrex.purplemusic.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PurpleDao {

    @Insert
    fun insertNowPlaying(nowPlaying: NowPlaying)

    @Insert
    fun insertNextUps(nextUpSongs: List<NextUpSongs>)

    @Query("SELECT * FROM NowPlaying")
    fun getNowPlaying() : Flow<NowPlaying>

    @Query("SELECT * FROM NextUpSongs")
    fun getNextUps() : Flow<List<NextUpSongs>>

    @Query("DELETE FROM NowPlaying")
    fun deleteNowPlay()

    @Query("DELETE FROM NextUpSongs")
    fun deleteNextUps()

    //Playlist starts here

    @Insert
    fun insertPlaylist(playlist: Playlist)

    @Insert
    fun insertToPlaylist(playlist: List<Playlist>)

    @Query("SELECT * FROM Playlist WHERE playlistItem=:playlistItem")
    fun getPlaylistItem(playlistItem: Boolean) : Flow<List<Playlist>>

    @Query("SELECT 4 FROM Playlist WHERE playlistId=:playlistId")
    fun getPlaylistItemImages(playlistId: Long) : Flow<List<Playlist>>

}