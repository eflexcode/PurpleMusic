package com.larrex.purplemusic.domain.model

import android.net.Uri

data class SongItem(
    val songUri: Uri,
    val songName: String,
    val artistName: String,
    val songCoverImageUri: Uri,
    val size: Int,
    val duration: Int
)
