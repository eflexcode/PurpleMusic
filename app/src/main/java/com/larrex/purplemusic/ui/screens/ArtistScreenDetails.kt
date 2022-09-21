package com.larrex.purplemusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.AlbumItem
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@Composable
fun ArtistScreenDetails(navController: NavController, artistName: String?) {

    val viewModel = hiltViewModel<MusicViewModel>()
    var name: String = ""
    if (artistName != null) {
        name = artistName
    }

    val albumsFromArtist by viewModel.getAllAlbumsFromArtist(name)
        .collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 137.dp)
    ) {

        Column() {

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack() }, modifier = Modifier
                        .padding(top = 10.dp, end = 5.dp, start = 5.dp, bottom = 0.dp)
                        .weight(0.3f)
                        .size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null
                    )

                }

                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    color = Util.TextColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 5.dp, start = 5.dp, top = 0.dp)
                        .weight(2f)
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Util.BottomBarBackground)
                    .padding(top = 0.dp)
            ) {

                items(albumsFromArtist) {

                    AlbumItem(albumItem = it) {
                        navController.currentBackStackEntry?.savedStateHandle?.set("album", it)

                        navController.navigate(BottomBarScreens.AlbumDetailsScreen.route)
                    }

                }
            }
        }
    }

}