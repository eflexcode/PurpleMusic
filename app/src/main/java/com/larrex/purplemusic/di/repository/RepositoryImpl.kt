package com.larrex.purplemusic.di.repository

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.domain.room.PurpleDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private var application: Application, private var database: PurpleDatabase
) : Repository {

    private val TAG = "RepositoryImpl"
    val thread = Thread()

    override fun getAllSongs(): Flow<List<SongItem>> {

        return flow<List<SongItem>> {

            val projection = arrayOf<String>(
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
            )

            val songs: MutableList<SongItem> = ArrayList()

            val songLibrary: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                songLibrary = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                songLibrary = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val order = "${MediaStore.Audio.Media.TITLE} ASC"

            val query = application.contentResolver.query(
                songLibrary, projection, null, null, order
            )!!

            query.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)
                    val name = cursor.getString(nameColumn) + " "
                    val title = cursor.getString(titleColumn) + " "
                    val artist = cursor.getString(artistColumn) + " "

                    val songUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), albumId
                    )

                    val songItem = SongItem(songUri, title, artist, imageUri, size, duration)

                    songs.add(songItem)

                }
                emit(songs)
            }

        }.flowOn(Dispatchers.IO)

    }

    override fun getAllAlbums(): Flow<List<AlbumItem>> {

        return flow<List<AlbumItem>> {
            val albums: MutableList<AlbumItem> = ArrayList()

            val projection = arrayOf(
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums._ID
            )

            val libraryUri: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                libraryUri = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                libraryUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
            }

            val order = MediaStore.Audio.Albums.ALBUM + " ASC"

            val query = application.contentResolver.query(libraryUri, projection, null, null, order)

            query?.use {

                val albumNameColumn = it.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
//                val albumIdColumn = it.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
                val idColumn = it.getColumnIndex(MediaStore.Audio.Albums._ID)
                val artistColumn = it.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
                val songsNumberColumn = it.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

                while (it.moveToNext()) {

                    val albumName = it.getString(albumNameColumn)
                    val id = it.getLong(idColumn)
                    val artist = it.getString(artistColumn)
                    val songNumber = it.getInt(songsNumberColumn)
//                    val albumId = it.getLong(albumIdColumn)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), id
                    )

                    val album = AlbumItem(artist, imageUri, id, albumName, songNumber)

                    albums.add(album)

                }
                emit(albums)
            }

        }.flowOn(Dispatchers.IO)

    }

    override fun getAllArtist(): Flow<List<ArtistItemModel>> {
        return flow<List<ArtistItemModel>> {

            val artistNames: MutableList<String> = ArrayList()
            val artists: MutableList<ArtistItemModel> = ArrayList()
            val artistsNew: MutableList<ArtistItemModel> = ArrayList()

            val projection = arrayOf(
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists._ID,
            )

            val libraryUri: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                libraryUri = MediaStore.Audio.Artists.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                libraryUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            }

            val order = MediaStore.Audio.Artists.ARTIST + " ASC"

            val query = application.contentResolver.query(libraryUri, projection, null, null, order)

            query?.use { it ->

                val artistNameColumn = it.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
                val idColumn = it.getColumnIndex(MediaStore.Audio.Artists._ID)
                val songsNumberColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
                val albumNumberColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)

                while (it.moveToNext()) {

                    val artistName = it.getString(artistNameColumn)
                    val id = it.getLong(idColumn)
                    val songNumber = it.getInt(songsNumberColumn)
                    val albumNumber = it.getInt(albumNumberColumn)

                    artistNames.add(artistName)

                    val imageUri: Uri = Uri.parse("")

                    val albums: MutableList<String> = ArrayList<String>()

                    val artistItemModel =
                        ArtistItemModel(imageUri, albumNumber, songNumber, artistName)


                    artists.add(artistItemModel)

                }

            }

            query?.close()

            for (item in artists) {
                val projectionArt = arrayOf(
                    MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID
                )

                val libraryUriArt: Uri

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    libraryUriArt =
                        MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    libraryUriArt = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
                }

                val orderArt = MediaStore.Audio.Albums.ALBUM + " ASC"
                val where = "${MediaStore.Audio.Albums.ARTIST} =?"
                val whereVal = arrayOf(item.artistName)

                val queryArt = application.contentResolver.query(
                    libraryUriArt, projectionArt, where, whereVal, orderArt
                )

                queryArt?.use {
                    val idColumnArt = it.getColumnIndex(MediaStore.Audio.Albums._ID)

                    while (it.moveToNext()) {
                        val idArt = it.getLong(idColumnArt)

                        val imageUri: Uri = ContentUris.withAppendedId(
                            Uri.parse("content://media/external/audio/albumart"), idArt
                        )
                        item.coverImageUri = imageUri


                    }


                }

            }

            emit(artists)

        }.flowOn(Dispatchers.IO)
    }

    override fun getAllSongsFromAlbum(albumName: String): Flow<List<SongItem>> {

        return flow<List<SongItem>> {

            Log.d(TAG, "getAllSongsFromAlbum: $albumName")

            val projection = arrayOf<String>(
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
            )

            val songs: MutableList<SongItem> = ArrayList()

            val songLibrary: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                songLibrary = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                songLibrary = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val order = "${MediaStore.Audio.Media.TITLE} ASC"
            val where = "${MediaStore.Audio.Albums.ALBUM} =?"
            val whereVal = arrayOf(albumName)

            val query = application.contentResolver.query(
                songLibrary, projection, where, whereVal, order
            )!!

            query.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)
                    val name = cursor.getString(nameColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)

                    val songUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), albumId
                    )

                    val songItem = SongItem(songUri, title, artist, imageUri, size, duration)

                    songs.add(songItem)

                }
                emit(songs)
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun getAllAlbumFromArtist(artistName: String): Flow<List<AlbumItem>> {
        return flow<List<AlbumItem>> {
            val albums: MutableList<AlbumItem> = ArrayList()

            val projection = arrayOf(
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums._ID
            )

            val libraryUri: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                libraryUri = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                libraryUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
            }

            val order = MediaStore.Audio.Albums.ALBUM + " ASC"
            val where = "${MediaStore.Audio.Albums.ARTIST} =?"
            val whereVal = arrayOf(artistName)
            val query =
                application.contentResolver.query(libraryUri, projection, where, whereVal, order)

            query?.use {

                val albumNameColumn = it.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
                val idColumn = it.getColumnIndex(MediaStore.Audio.Albums._ID)
                val artistColumn = it.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
                val songsNumberColumn = it.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

                while (it.moveToNext()) {

                    val albumName = it.getString(albumNameColumn)
                    val id = it.getLong(idColumn)
                    val artist = it.getString(artistColumn)
                    val songNumber = it.getInt(songsNumberColumn)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), id
                    )

                    val album = AlbumItem(artist, imageUri, id, albumName, songNumber)

                    albums.add(album)

                }
                emit(albums)
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun insertNowPlaying(nowPlaying: NowPlaying) {

        database.dao().insertNowPlaying(nowPlaying)

    }

    override fun insertPlaylist(playlist: Playlist) {

        database.dao().insertPlaylist(playlist)

    }

    override fun insertNextUps(nextUpSongs: List<NextUpSongs>) {

        database.dao().insertNextUps(nextUpSongs)
    }

    override fun getNowPlaying(): Flow<NowPlaying> {

        return database.dao().getNowPlaying()
    }

    override fun getNextUps(): Flow<List<NextUpSongs>> {

        return database.dao().getNextUps()

    }

    override fun getPlaylistItem(): Flow<List<Playlist>> {

      return  database.dao().getPlaylistItem(true)

    }

    override fun deleteNowPlay() {

        database.dao().deleteNowPlay()

    }

    override fun deleteNextUps() {

        database.dao().deleteNextUps()

    }

}