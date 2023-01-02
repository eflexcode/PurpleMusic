package com.larrex.purplemusic.domain.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NextUpSongs")
data class  NextUpSongs(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val songUri: String,
    val songName: String,
    val artistName: String,
    val songCoverImageUri: String,
    val size: Int,
    val duration: Float
)
