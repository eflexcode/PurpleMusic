package com.larrex.purplemusic.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larrex.purplemusic.ui.screens.AlbumScreen
import com.larrex.purplemusic.ui.screens.FavouriteScreen
import com.larrex.purplemusic.ui.screens.MusicScreen
import com.larrex.purplemusic.ui.screens.SearchScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomBarScreens.MusicScreen.route) {

        composable(route = BottomBarScreens.MusicScreen.route) {
            MusicScreen()
        }
        composable(route = BottomBarScreens.FavouriteScreen.route) {
            FavouriteScreen()
        }
        composable(route = BottomBarScreens.AlbumScreen.route) {
            AlbumScreen()
        }
        composable(route = BottomBarScreens.SearchScreen.route) {
            SearchScreen()
        }

    }

}