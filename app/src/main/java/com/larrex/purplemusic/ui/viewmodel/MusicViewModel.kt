package com.larrex.purplemusic.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MusicViewModel"

@HiltViewModel
class MusicViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    val playlistItemImages = mutableStateListOf<String>()
    var canLoad by mutableStateOf(false)

    fun getAllSongs(): Flow<List<SongItem>> {

        return repository.getAllSongs()
    }

    fun getAllSongsFromAlbum(albumName: String): Flow<List<SongItem>> {

        return repository.getAllSongsFromAlbum(albumName)
    }

    fun getAllAlbums(): Flow<List<AlbumItem>> {

        return repository.getAllAlbums()
    }

    fun getAllAlbumsFromArtist(artistName: String): Flow<List<AlbumItem>> {

        return repository.getAllAlbumFromArtist(artistName)
    }

    fun getAllArtist(): Flow<List<ArtistItemModel>> {

        return repository.getAllArtist()
    }

    fun insertNowPlaying(nowPlaying: NowPlaying) {

        repository.insertNowPlaying(nowPlaying)

    }

    fun deleteNowPlaying() {

        repository.deleteNowPlay()

    }

    fun getNowPlaying(): Flow<NowPlaying> {

        return repository.getNowPlaying()
    }

    fun insertNextUps(nextUps: List<NextUpSongs>) {

        repository.insertNextUps(nextUps)

    }

    fun getNextUps(): Flow<List<NextUpSongs>> {

        return repository.getNextUps()
    }

    fun deleteNextUps() {
        repository.deleteNextUps()
    }

    fun insertPlaylist(playlist: Playlist) {

        repository.insertPlaylist(playlist)

    }

    fun insertToPlaylist(playlist: List<Playlist>) {

        repository.insertToPlaylist(playlist)

    }

    fun getPlaylistItem(): Flow<List<Playlist>> {
        return repository.getPlaylistItem()
    }

    fun getPlaylistItemImages(playlistId: Long): Flow<List<Playlist>> {
        Log.d(TAG, "getPlaylistItemImages: $playlistId")
          return  repository.getPlaylistItemImages(playlistId)

    }

    fun getPlaylistContentWithId(playlistId: Long) : Flow<List<Playlist>>{
        return  repository.getPlaylistContentWithId(playlistId)
    }

}