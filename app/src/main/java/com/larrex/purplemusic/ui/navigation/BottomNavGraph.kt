package com.larrex.purplemusic.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.ui.screens.*
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@Composable
fun BottomNavGraph(navController: NavHostController, viewModel: MusicViewModel) {

    NavHost(navController = navController, startDestination = BottomBarScreens.MusicScreen.route) {

        composable(route = BottomBarScreens.MusicScreen.route) {
            MusicScreen(navController,viewModel)
        }
        composable(route = BottomBarScreens.FavouriteScreen.route) {
            FavouriteScreen(viewModel,navController)
        }
        composable(route = BottomBarScreens.AlbumScreen.route) {
            AlbumScreen(navController)
        }
        composable(route = BottomBarScreens.ArtistScreen.route) {
            ArtistScreen(navController)
        }
        composable(route = BottomBarScreens.AlbumDetailsScreen.route) {

            val album = navController.previousBackStackEntry?.savedStateHandle?.get<AlbumItem>("album")

            AlbumDetailsScreen(album, navController,viewModel)

        }
        composable(route = BottomBarScreens.ArtistDetailsScreen.route) {

            val name = navController.previousBackStackEntry?.savedStateHandle?.get<String>("artistName")

            ArtistScreenDetails(navController,name)

        }
        composable(BottomBarScreens.NowPlayingScreen.route){
            NowPlayingScreen(navController)
        }
        composable(BottomBarScreens.PlaylistDetailsScreen.route){

            val id = navController.previousBackStackEntry?.savedStateHandle?.get<Long>("playlistId")

            if (id != null) {
                PlaylistDetailsScreen(navController,viewModel,id)
            }
        }
    }

}