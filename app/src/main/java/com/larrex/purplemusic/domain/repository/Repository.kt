package com.larrex.purplemusic.domain.repository

import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.Playlist
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllSongs(): Flow<List<SongItem>>

    fun getAllAlbums(): Flow<List<AlbumItem>>

    fun getAllArtist(): Flow<List<ArtistItemModel>>

    fun getAllSongsFromAlbum(albumName: String): Flow<List<SongItem>>

    fun getAllAlbumFromArtist(artistName: String): Flow<List<AlbumItem>>

    fun insertNowPlaying(nowPlaying: NowPlaying)

    fun insertPlaylist(playlist: Playlist)

    fun insertToPlaylist(playlist: List<Playlist>)

    fun getPlaylistItemImages(playlistId: Long): Flow<List<Playlist>>

    fun getPlaylistContentWithId(playlistId: Long): Flow<List<Playlist>>

    fun insertNextUps(nextUpSongs: List<NextUpSongs>)

    fun getNowPlaying(): Flow<NowPlaying>

    fun getNextUps(): Flow<List<NextUpSongs>>

    fun getPlaylistItem(): Flow<List<Playlist>>

    fun deleteNowPlay()

    fun deleteNextUps()

    fun deleteSingleItemFromAPlaylist(id: Int)

    fun deleteAPlaylist(ids: List<Int>)

    fun updateRepeat(id: Int, repeat: Int)

    fun updateShuffle(id: Int, shuffle: Boolean)

    fun updateNowPlaying(
        id: Int,
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Float,
    )

    fun updateNowPlayingWithTypeAndName(
        id: Int,
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Float,
        playingFromType: String,
        playingFromName: String,
    )
}