package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.ArtistItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

private const val TAG = "ArtistScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ArtistScreen(navController: NavController, viewModel: MusicViewModel) {

//    val viewModel = hiltViewModel<MusicViewModel>()

    val artists by viewModel.getAllArtist().collectAsState(initial = emptyList())
    val permissionList = if (Build.VERSION.SDK_INT >= 33) {
        listOf(
            Manifest.permission.READ_MEDIA_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val storagePermission = rememberMultiplePermissionsState(permissionList)

    Box(

        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 137.dp), contentAlignment = Alignment.Center

    ) {
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

                Text(text = "Give Permission")

            }

        }

    } else {

            if (artists.isEmpty()) {
                CircularProgressIndicator()
            }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(), columns = GridCells.Fixed(2)
            ) {

                items(artists) {

                    ArtistItem(artistItemModel = it) {

                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "artistName",
                            it.artistName
                        )
                        navController.navigate(BottomBarScreens.ArtistDetailsScreen.route)
                    }

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