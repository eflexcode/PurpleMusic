package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.os.Build
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.nowplayingroom.NextUpSongs
import com.larrex.purplemusic.domain.room.nowplayingroom.NowPlaying
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MusicScreen"

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(navController: NavController) {

    val chipItems = listOf("Music", "Albums")
    var newText by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val nextUpSongs: MutableList<NextUpSongs> = ArrayList<NextUpSongs>()


    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 137.dp), contentAlignment = Alignment.Center
    ) {
        val viewModel = hiltViewModel<MusicViewModel>()

        val readState =
            rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionList = if (Build.VERSION.SDK_INT >= 33) {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
                    modifier = Modifier.padding(10.dp), color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { storagePermission.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Purple
                    )
                ) {

                    Text(text = "Give Permissions")

                }

            }

        } else {

            val musicItems by viewModel.getAllSongs().collectAsState(initial = emptyList())

//            for (song in musicItems) {
//                nextUpSongs.add(
//                    NextUpSongs(
//                        null,
//                        song.songUri.toString(), song.songName,
//                        song.artistName, song.songCoverImageUri.toString(), song.size, song.duration
//                    )
//                )
//            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                item {

                    if (musicItems.isNotEmpty()) {
                        TextField(
                            value = newText,
                            onValueChange = { text ->
                                newText = text
                            },
                            modifier = Modifier
                                .padding(top = 5.dp, end = 20.dp, start = 20.dp, bottom = 5.dp)
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

                items(musicItems) {

                    MusicItem(onClicked = {

                        navController.navigate(BottomBarScreens.NowPlayingScreen.route)
                        CoroutineScope(Dispatchers.IO).launch {

                            val nowPlaying = NowPlaying(
                                null,
                                it.songUri.toString(),
                                it.songName, it.artistName,
                                it.songCoverImageUri.toString(),
                                it.duration, 0, false, false
                            )

                            viewModel.deleteNowPlaying()
                            viewModel.deleteNextUps()

                            viewModel.insertNowPlaying(nowPlaying)

                            musicItems.forEach { song->

                                nextUpSongs.add(
                                    NextUpSongs(
                                        null,
                                        song.songUri.toString(), song.songName,
                                        song.artistName, song.songCoverImageUri.toString(), song.size, song.duration
                                    )
                                )
                            }.apply {
                                viewModel.insertNextUps(nextUpSongs)
                            }

                        }

                    }, onLongClicked = {
                        Log.d(TAG, "MusicScreen: " + it.songName)
                    }, it)

                }

            }

        }
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Composable
fun Font() {
    var navController: NavController = rememberNavController()

    MusicScreen(navController)
}