package com.larrex.purplemusic.domain.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.larrex.purplemusic.domain.model.SongItem

@Entity(tableName = "NowPlaying")
data class NowPlaying(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var musicUri: String,
    var musicName: String,
    var artistName: String,
    var albumArt: String,
    var duration: Int,
    var currentDuration : Int,
    var repeat: Boolean,
    var shuffle: Boolean,
){

}