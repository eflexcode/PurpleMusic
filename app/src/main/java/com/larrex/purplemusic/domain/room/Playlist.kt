package com.larrex.purplemusic.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/*Please note  playlist item details is here too*/

@Entity(tableName = "Playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val songUri: String,
    val songName: String,
    val artistName: String,
    val albumName: String,
    val songCoverImageUri: String,
    val size: Int,
    val duration: Int,
    val playlistItem: Boolean,
    val playlistId: Long,
    val playlistName: String
)
