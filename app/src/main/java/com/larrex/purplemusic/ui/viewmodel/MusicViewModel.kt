package com.larrex.purplemusic.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.*
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

    val searchSongsList = mutableStateListOf<SongItem>()
    var allSongsList: List<SongItem> = ArrayList<SongItem>()

    val searchNextUpList = mutableStateListOf<NextUpSongs>()
    var allNextUpList: List<NextUpSongs> = ArrayList<NextUpSongs>()

    var canLoad by mutableStateOf(false)

    init {

        CoroutineScope(Dispatchers.IO).launch {

            getAllSongs().collectLatest {
                allSongsList = it
            }

            getNextUps().collectLatest {
                allNextUpList = it
            }

        }
    }


    fun searchSongs(songName: String) {

        searchSongsList.clear()

        allSongsList.forEach {

            if (it.songName.toLowerCase().contains(songName.toLowerCase())) {
                searchSongsList.add(it)
            }

        }

    }

    fun searchNextUps(songName: String) {

        searchNextUpList.clear()

        allNextUpList.forEach {

            if (it.songName.toLowerCase().contains(songName.toLowerCase())) {
                searchNextUpList.add(it)
            }

        }

    }

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
        return repository.getPlaylistItemImages(playlistId)

    }

    fun getPlaylistContentWithId(playlistId: Long): Flow<List<Playlist>> {
        return repository.getPlaylistContentWithId(playlistId)
    }

    fun deleteSingleItemFromAPlaylist(id: Int) {
        repository.deleteSingleItemFromAPlaylist(id)
    }

    fun deleteAPlaylist(ids: List<Int>) {
        repository.deleteAPlaylist(ids)
    }
}