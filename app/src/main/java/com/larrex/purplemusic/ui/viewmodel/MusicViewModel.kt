package com.larrex.purplemusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private var repository: Repository): ViewModel(){

    fun getAllSongs(): Flow<List<SongItem>> {

        return repository.getAllSongs()
    }

}