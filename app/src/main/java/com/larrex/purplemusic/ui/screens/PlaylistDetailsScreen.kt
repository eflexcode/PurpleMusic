package com.larrex.purplemusic.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.CustomGridImages
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.screens.component.MusicItemPlaylist
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PlaylistDetailsScreen"

@Composable
fun PlaylistDetailsScreen(navController: NavController, viewModel: MusicViewModel, playlist: Long) {

    val songs by viewModel.getPlaylistContentWithId(playlist)
        .collectAsState(initial = emptyList())

    val images by viewModel.getPlaylistItemImages(playlist)
        .collectAsState(initial = emptyList())

    Log.d(TAG, "PlaylistDetailsScreen: $images")

    val nowPlaying by viewModel.getNowPlaying().collectAsState(null)

    val nextUpSongs: MutableList<NextUpSongs> = ArrayList<NextUpSongs>()

    for (song in songs) {
        if (!song.playlistItem)

            nextUpSongs.add(
                NextUpSongs(
                    null,
                    song.songUri.toString(), song.songName,
                    song.artistName, song.songCoverImageUri.toString(), song.size, song.duration
                )
            )
    }

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Util.BottomBarBackground),
            contentPadding = PaddingValues(bottom = 150.dp)
        ) {

            item {
                //back button
                IconButton(
                    onClick = { navController.popBackStack() }, modifier = Modifier
                        .padding(top = 10.dp, end = 5.dp, start = 5.dp, bottom = 0.dp)
                        .size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Card(
                        modifier = Modifier
                            .background(Util.BottomBarBackground)
                            .size(height = 220.dp, width = 220.dp)
                            .padding(top = 10.dp, end = 5.dp, start = 5.dp, bottom = 5.dp),
                        colors = CardDefaults.cardColors(containerColor = Util.BottomBarBackground),
                        elevation = CardDefaults.cardElevation(10.dp),

                        ) {
                        if (images.isNotEmpty())

                            CustomGridImages(
                                images = images, modifier = Modifier
                                    .size(220.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )

                    }

                    if (songs.isNotEmpty())
                        Text(
                            text = songs[0].playlistName,
                            fontSize = 23.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = Util.TextColor,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                end = 20.dp,
                                start = 20.dp,
                                top = 10.dp,
                                bottom = 20.dp
                            )
                        )

                    Button(
                        onClick = {

                            CoroutineScope(Dispatchers.IO).launch {

                                if (songs.size > 1) {

                                    nowPlaying?.id?.let {
                                        updateNowPlayingPlaylist(
                                            it,songs[1].songUri,
                                            songs[1].songName, songs[1].artistName,
                                            songs[1].songCoverImageUri,
                                            songs[1].duration,  "Playlist", songs[0].playlistName, viewModel)
                                    }
                                    viewModel.deleteNextUps()
                                    viewModel.insertNextUps(nextUpSongs)

                                }
                            }
                            navController.navigate(BottomBarScreens.NowPlayingScreen.route)

                        },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(bottom = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Purple
                        )
                    ) {

                        Text(text = "Play all")

                    }

                }

            }

            items(songs) { item ->

                if (!item.playlistItem)

                    MusicItemPlaylist(onClicked = {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (songs.size > 1) {

                                nowPlaying?.id?.let {
                                    updateNowPlayingPlaylist(
                                        it, item.songUri.toString(),
                                        item.songName, item.artistName,
                                        item.songCoverImageUri.toString(),
                                        item.duration,  "Playlist", songs[0].playlistName, viewModel)
                                }
                                viewModel.deleteNextUps()
                                viewModel.insertNextUps(nextUpSongs)

                            }
                        }
                        navController.navigate(BottomBarScreens.NowPlayingScreen.route)

                    }, songItem = item,viewModel)

            }

        }
    }


}
fun updateNowPlayingPlaylist(
    id: Int,
    musicUri: String,
    musicName: String,
    artistName: String,
    albumArt: String,
    duration: Float,
    playingFromType: String,
    playingFromName: String,viewModel: MusicViewModel
) {

    viewModel.updateNowPlayingWithTypeAndName(id, musicUri, musicName, artistName, albumArt, duration, playingFromType, playingFromName)

}