package com.larrex.purplemusic.domain.repository

import android.app.Application
import com.larrex.purplemusic.domain.model.SongItem
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllSongs() : Flow<List<SongItem>>

}