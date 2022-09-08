package com.larrex.purplemusic.di.repository

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private var application: Application) : Repository {

    override fun getAllSongs(): Flow<List<SongItem>> {

        return flow<List<SongItem>> {

            val projection = arrayOf<String>(
                MediaStore.Audio.Media.DISPLAY_NAME,
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

            val order = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

            val query = application.contentResolver.query(
                songLibrary, projection, null, null, order
            )!!

            query.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME )
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
                    val artist = cursor.getString(artistColumn)

                    val songUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    val imageUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )

                    val realName = name.substring(0,name.lastIndexOf("."))

                    realName.replace(artist,"")

                    val songItem = SongItem(songUri, realName, artist, imageUri, size, duration)

                    songs.add(songItem)

                }
                emit(songs)
            }

        }.flowOn(Dispatchers.IO)

    }
}