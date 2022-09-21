package com.larrex.purplemusic.ui.navigation

import com.larrex.purplemusic.R

sealed class BottomBarScreens(val route: String,
                              val barTitle: String,
                              iconResourceId: Int){

    object MusicScreen : BottomBarScreens("music","Music", R.drawable.ic_music)
    object AlbumScreen : BottomBarScreens("albums","Albums", R.drawable.ic_album)
    object ArtistScreen : BottomBarScreens("artists","Artists", R.drawable.ic_search)
    object FavouriteScreen : BottomBarScreens("favourite","Favourite", R.drawable.ic_favourite)
    object AlbumDetailsScreen : BottomBarScreens("album_details","", 0)
    object ArtistDetailsScreen : BottomBarScreens("artist_details","", 0)
    object NowPlayingScreen : BottomBarScreens("nowPlaying","", 0)

}
