package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.screens.component.PickSongsFloatingItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MusicScreen"

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
)
@Composable
fun MusicScreen(navController: NavController, viewModel: MusicViewModel) {

    val chipItems = listOf("Music", "Albums")
    var newText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCount by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val nextUpSongs: MutableList<NextUpSongs> = ArrayList<NextUpSongs>()
    val playlists: MutableList<Playlist> = ArrayList<Playlist>()
    val playlistsWithId: MutableList<Playlist> = ArrayList<Playlist>()

    var visibleState = remember { MutableTransitionState(false).apply { targetState = false } }

    var nowPlaying: NowPlaying? = null
    val nowPlaying2 by viewModel.getNowPlaying().collectAsState(null)
    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 137.dp), contentAlignment = Alignment.Center
    ) {

        val readState =
            rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionList = if (Build.VERSION.SDK_INT >= 33) {
            listOf(
                Manifest.permission.READ_MEDIA_AUDIO,
//                Manifest.permission.WRITE_ME,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }


        val storagePermission = rememberMultiplePermissionsState(permissionList)

        if (!storagePermission.allPermissionsGranted) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Storage permission is required to load audio files",
                    modifier = Modifier.padding(10.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { storagePermission.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White, containerColor = Purple
                    )
                ) {

                    Text(text = "Give Permissions")

                }

            }

        } else {

            val musicItems by viewModel.getAllSongs().collectAsState(initial = emptyList())

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(60.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (musicItems.isNotEmpty()) {
                    TextField(value = newText,
                        onValueChange = { text ->
                            newText = text
                            viewModel.searchSongs(newText.text)
                        },
                        modifier = Modifier
                            .padding(
                                top = 5.dp, end = 20.dp, start = 20.dp, bottom = 5.dp
                            )
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            contentColorFor(backgroundColor = Color.Transparent),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Util.searchBarBackground,
                            placeholderColor = Color.Gray,
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text(text = "Search music", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null, modifier = Modifier.size(20.dp),
                            )
                        }

                    )


                } else {
                    CircularProgressIndicator()
                }

            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 61.dp)
            ) {
                items(
                    if (newText.text.trim()
                            .isEmpty()
                    ) musicItems else viewModel.searchSongsList
                ) { item ->

                    MusicItem(onClicked = {
                        CoroutineScope(Dispatchers.IO).launch {

                            if (nowPlaying2 == null) {
                                nowPlaying = NowPlaying(
                                    null,
                                    item.songUri.toString(),
                                    item.songName,
                                    item.artistName,
                                    item.songCoverImageUri.toString(),
                                    item.duration.toLong(),
                                    0,
                                    2,
                                    false, "Device", "All Songs", true
                                )
                                viewModel.insertNowPlaying(nowPlaying!!)
                            } else {

                                nowPlaying2?.id?.let {
                                    updateNowPlayingMusic(
                                        it, item.songUri.toString(),
                                        item.songName,
                                        item.artistName,
                                        item.songCoverImageUri.toString(),
                                        item.duration, "Device", "All Songs", viewModel
                                    )
                                }
                            }
                            viewModel.deleteNextUps()

                            musicItems.forEach { song ->

                                nextUpSongs.add(
                                    NextUpSongs(
                                        null,
                                        song.songUri.toString(),
                                        song.songName,
                                        song.artistName,
                                        song.songCoverImageUri.toString(),
                                        song.size,
                                        song.duration
                                    )
                                )

                            }.apply {

                                viewModel.insertNextUps(nextUpSongs)

                            }


                        }
                        viewModel.play()

                    }, onLongClicked = {

                        Log.d(TAG, "MusicScreen: " + item.songName)
                        visibleState.targetState = true
                        selectedCount++

                        CoroutineScope(Dispatchers.IO).launch {

                            nextUpSongs.add(
                                NextUpSongs(
                                    null,
                                    item.songUri.toString(),
                                    item.songName,
                                    item.artistName,
                                    item.songCoverImageUri.toString(),
                                    item.size,
                                    item.duration
                                )
                            )

                            nowPlaying = NowPlaying(
                                null,
                                item.songUri.toString(),
                                item.songName,
                                item.artistName,
                                item.songCoverImageUri.toString(),
                                item.duration.toLong(),
                                0,
                                2,
                                false, "Device", "All Songs", true
                            )

                            //add playlist
                            playlists.add(
                                Playlist(
                                    songCoverImageUri = item.songCoverImageUri.toString(),
                                    playlistId = 0,
                                    songName = item.songName,
                                    songUri = item.songUri.toString(),
                                    albumName = "",
                                    artistName = item.artistName,
                                    playlistItem = false,
                                    duration = item.duration,
                                    size = item.size,
                                    playlistName = ""
                                )
                            )

                        }
                    }, onUnselected = {

                        selectedCount--

                        if (selectedCount == 0) {
                            visibleState.targetState = false
                            playlists.clear()
                            return@MusicItem
                        }

                        for (song in nextUpSongs) {

                            if (song.songName == it) {

                                nextUpSongs.remove(song)

                                CoroutineScope(Dispatchers.IO).launch {

                                    nowPlaying = NowPlaying(
                                        null,
                                        nextUpSongs[0].songUri.toString(),
                                        nextUpSongs[0].songName,
                                        nextUpSongs[0].artistName,
                                        nextUpSongs[0].songCoverImageUri.toString(),
                                        nextUpSongs[0].duration.toLong(),
                                        0,
                                        1,
                                        false, "Device", "Marked Songs", true
                                    )


                                }

                                return@MusicItem

                            }
                        }


                    }, item, false)

                }

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                AnimatedVisibility(
                    visibleState = visibleState, enter = scaleIn(), exit = scaleOut()
                ) {
                    PickSongsFloatingItem(selectedCount, viewModel, addToPlaylist = {

                        CoroutineScope(Dispatchers.IO).launch {

                            playlists.forEach { song ->

                                playlistsWithId.add(
                                    Playlist(
                                        songCoverImageUri = song.songCoverImageUri.toString(),
                                        playlistId = it,
                                        songName = song.songName,
                                        songUri = song.songUri.toString(),
                                        albumName = "",
                                        artistName = song.artistName,
                                        playlistItem = false,
                                        duration = song.duration,
                                        size = song.size,
                                        playlistName = ""
                                    )
                                )

                            }.apply {

                                viewModel.insertToPlaylist(playlistsWithId)
                                visibleState.targetState = false
                            }

                        }
                    }) {

                        CoroutineScope(Dispatchers.IO).launch {

                            nowPlaying2?.id?.let {
                                nowPlaying?.let { it1 ->
                                    updateNowPlayingMusic(
                                        it, nowPlaying?.musicUri.toString(),
                                        it1.musicName,
                                        nowPlaying!!.artistName,
                                        nowPlaying!!.albumArt.toString(),
                                        nowPlaying!!.duration.toFloat(),
                                        nowPlaying!!.playingFromType,
                                        nowPlaying!!.playingFromName, viewModel
                                    )
                                }
                            }

                            viewModel.deleteNextUps()
                            viewModel.insertNextUps(nextUpSongs)


                            visibleState.targetState = false
                        }
                        navController.navigate(BottomBarScreens.NowPlayingScreen.route)
                    }
                }

            }
        }
    }
}

fun updateNowPlayingMusic(
    id: Int,
    musicUri: String,
    musicName: String,
    artistName: String,
    albumArt: String,
    duration: Float,
    playingFromType: String,
    playingFromName: String, viewModel: MusicViewModel
) {

    viewModel.updateNowPlayingWithTypeAndName(
        id,
        musicUri,
        musicName,
        artistName,
        albumArt,
        duration,
        playingFromType,
        playingFromName
    )

}

@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Composable
fun Font() {

    var navController: NavController = rememberNavController()

//    MusicScreen(navController)
}