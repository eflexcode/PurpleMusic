package com.larrex.purplemusic.domain.model

import android.net.Uri

data class ArtistItemModel(
    var coverImageUri: Uri,
    val albumsCount: Int,
    val songsCount: Int,
    val artistName: String,
) {

}