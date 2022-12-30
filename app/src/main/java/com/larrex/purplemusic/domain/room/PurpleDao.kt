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
    fun getNowPlaying(): Flow<NowPlaying>

    @Query("UPDATE NowPlaying SET repeat=:repeat WHERE id = :id")
    fun updateNowPlayingRepeat(id : Int,repeat : Int)

    @Query("UPDATE NowPlaying SET shuffle=:shuffle WHERE id = :id")
    fun updateNowPlayingShuffle(id : Int,shuffle : Boolean)

    @Query("SELECT * FROM NextUpSongs")
    fun getNextUps(): Flow<List<NextUpSongs>>

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
    fun getPlaylistItem(playlistItem: Boolean): Flow<List<Playlist>>

    @Query("SELECT * FROM Playlist WHERE playlistId=:playlistId Limit 5")
    fun getPlaylistItemImages(playlistId: Long): Flow<List<Playlist>>

    @Query("SELECT * FROM Playlist WHERE playlistId=:playlistId ")
    fun getPlaylistContentWithId(playlistId: Long): Flow<List<Playlist>>

    @Query("DELETE FROM Playlist WHERE id=:id")
    fun deleteSingleItemFromAPlaylist(id: Int)

    @Query("DELETE FROM Playlist WHERE id in (:ids)")
    fun deleteAPlaylist(ids: List<Int>)

}