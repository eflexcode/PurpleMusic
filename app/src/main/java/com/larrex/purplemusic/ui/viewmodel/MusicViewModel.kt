package com.larrex.purplemusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

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

}