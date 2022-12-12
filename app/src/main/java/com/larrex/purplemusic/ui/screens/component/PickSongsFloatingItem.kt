package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@Composable
fun PickSongsFloatingItem(count: Int, viewModel: MusicViewModel,addToPlaylist:(playlistId : Long) -> Unit, play: () -> Unit) {

    val playlists by viewModel.getPlaylistItem().collectAsState(initial = emptyList())

    Surface(shadowElevation = 5.dp, shape = RoundedCornerShape(10.dp), modifier = Modifier) {

        Box(
            modifier = Modifier
                .background(
                    Util.PickSongsFloatingBackground,
                )
                .padding(10.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Long press to select or unselect",
                    fontSize = 13.sp,
                    color = Util.TextColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "$count Selected",
                        color = Util.TextColor,
                        fontSize = 18.sp,
                    )

                    IconButton(onClick = { play() }, modifier = Modifier.size(30.dp)) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_play),
                            contentDescription = null
                        )
                    }

                }

                Text(
                    text = "Or add to a playlist",
                    fontSize = 13.sp,
                    color = Util.TextColor
                )

                LazyColumn(Modifier.height(100.dp).width(200.dp).padding(top = 5.dp)) {

                    items(playlists) {

                        PlayListItem(playlist = it,true) {

                            addToPlaylist(it.playlistId)

                        }

                    }

                }

            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun PickSongsFloatingItemPreview() {

//    PickSongsFloatingItem(45) {}

}