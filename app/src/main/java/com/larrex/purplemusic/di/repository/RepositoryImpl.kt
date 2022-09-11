package com.larrex.purplemusic.di.repository

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private var application: Application) : Repository {

    private val TAG = "RepositoryImpl"

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
                    val name = cursor.getString(nameColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)

                    val songUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )

//                    val realName = name.substring(0, name.lastIndexOf("."))
//
//                    realName.replace(artist, "")

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
                        Uri.parse("content://media/external/audio/albumart"),
                        id
                    )

                    val album = AlbumItem(artist, imageUri, id, albumName, songNumber)

                    albums.add(album)

                }
                emit(albums)
            }

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
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )

//                    val realName = name.substring(0, name.lastIndexOf("."))
//
//                    realName.replace(artist, "")

                    val songItem = SongItem(songUri, title, artist, imageUri, size, duration)

                    songs.add(songItem)

                }
                emit(songs)
            }
        }.flowOn(Dispatchers.IO)

    }

}