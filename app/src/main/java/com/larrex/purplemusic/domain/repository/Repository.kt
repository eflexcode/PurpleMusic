package com.larrex.purplemusic.domain.repository

import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllSongs() : Flow<List<SongItem>>
    fun getAllAlbums() : Flow<List<AlbumItem>>
    fun getAllArtist() : Flow<List<ArtistItemModel>>
    fun getAllSongsFromAlbum(albumName : String) : Flow<List<SongItem>>
    fun getAllAlbumFromArtist(artistName : String) : Flow<List<AlbumItem>>

}