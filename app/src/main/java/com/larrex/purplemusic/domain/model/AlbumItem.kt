package com.larrex.purplemusic.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumItem(
    val artistName: String,
    val albumCoverImageUri: Uri,
    val albumId: Long,
    val albumName: String,
    val numberOfSongs: Int
) : Parcelable {
}