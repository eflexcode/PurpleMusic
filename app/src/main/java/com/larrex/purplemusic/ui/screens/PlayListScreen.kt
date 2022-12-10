package com.larrex.purplemusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.ui.screens.component.CreatePlaylist
import com.larrex.purplemusic.ui.screens.component.PlayListItem
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@Composable
fun FavouriteScreen(viewModel: MusicViewModel) {

    var isFABClicked by remember {
        mutableStateOf(false)
    }

    val playlists by viewModel.getPlaylistItem().collectAsState(initial = emptyList())

    if (isFABClicked) {

        CreatePlaylist() {status,name->
            isFABClicked = status

            val playlistId = System.currentTimeMillis()

            val mPlaylist = Playlist(
                songCoverImageUri = "",
                playlistId = playlistId,
                songName = "",
                songUri = "",
                albumName = "",
                artistName = "",
                playlistItem = true,
                duration = 0,
                size = 0,
                playlistName = name
            )

            viewModel.insertPlaylist(mPlaylist)

        }

    }

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 137.dp), contentAlignment = Alignment.BottomEnd

    ) {

        LazyColumn() {

            items(playlists){

                PlayListItem(playlist = it) {

                }

            }

        }

        FloatingActionButton(onClick = {

            isFABClicked = true

        }, modifier = Modifier.padding(30.dp)) {

            Icon(
                painter = painterResource(id = R.drawable.ic_round_add),
                contentDescription = "add playlist"
            )

        }

    }

}