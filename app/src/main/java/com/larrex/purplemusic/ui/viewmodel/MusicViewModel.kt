package com.larrex.purplemusic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

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

    var isVisible : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun isBarVisible(): LiveData<Boolean> {

        return isVisible
    }

    fun setBarVisible(isVisibleNew: Boolean){

        isVisible.value = isVisibleNew
    }




}