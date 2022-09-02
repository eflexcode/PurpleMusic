package com.larrex.purplemusic.ui.navigation

import com.larrex.purplemusic.R

sealed class BottomBarScreens(val route: String,
                              val barTitle: String,
                              iconResourceId: Int){

    object MusicScreen : BottomBarScreens("music","Music", R.drawable.ic_music)
    object FavouriteScreen : BottomBarScreens("favourite","Favourite", R.drawable.ic_favourite)
    object AlbumScreen : BottomBarScreens("album","Album", R.drawable.ic_album)
    object SearchScreen : BottomBarScreens("search","Search", R.drawable.ic_search)

}
