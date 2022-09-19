package com.larrex.purplemusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.ArtistItem
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

private const val TAG = "ArtistScreen"

@Composable
fun ArtistScreen(navController: NavController) {

    val viewModel = hiltViewModel<MusicViewModel>()

    val artists by viewModel.getAllArtist().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 138.dp), contentAlignment = Alignment.Center

    ) {
        if (artists.isEmpty()) {
            CircularProgressIndicator()
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(), columns = GridCells.Fixed(2)
        ) {

            items(artists) {

                ArtistItem(artistItemModel = it) {

                    navController.currentBackStackEntry?.savedStateHandle?.set("artistName",it.artistName)
                    navController.navigate(BottomBarScreens.ArtistDetailsScreen.route)
                }

            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun Previews() {
//    ArtistScreen()
}