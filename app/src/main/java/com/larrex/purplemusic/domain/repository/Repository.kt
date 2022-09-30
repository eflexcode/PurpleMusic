package com.larrex.purplemusic.domain.repository

import androidx.room.Insert
import androidx.room.Query
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.room.nowplayingroom.NextUpSongs
import com.larrex.purplemusic.domain.room.nowplayingroom.NowPlaying
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllSongs() : Flow<List<SongItem>>

    fun getAllAlbums() : Flow<List<AlbumItem>>

    fun getAllArtist() : Flow<List<ArtistItemModel>>

    fun getAllSongsFromAlbum(albumName : String) : Flow<List<SongItem>>

    fun getAllAlbumFromArtist(artistName : String) : Flow<List<AlbumItem>>

    fun insertNowPlaying(nowPlaying: NowPlaying)

    fun insertNextUps(nextUpSongs: List<NextUpSongs>)

    fun getNowPlaying() : Flow<NowPlaying>

    fun getNextUps() : Flow<List<NextUpSongs>>

    fun deleteNowPlay()

    fun deleteNextUps()
}