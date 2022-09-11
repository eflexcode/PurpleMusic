package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.AlbumItem
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import com.larrex.purplemusic.domain.model.AlbumItem

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlbumScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 147.dp)
    ) {

        val viewModel = hiltViewModel<MusicViewModel>()

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

                    Text(text = "Give Permission")

                }

            }

        } else {

            val albumItem by viewModel.getAllAlbums().collectAsState(initial = emptyList())

            LazyVerticalGrid(columns = GridCells.Fixed(2)) {

                items(albumItem) {

                    AlbumItem(albumItem = it) {

                        navController.currentBackStackEntry?.savedStateHandle?.set("album", it)

                        navController.navigate(BottomBarScreens.AlbumDetailsScreen.route)
                    }

                }

            }

        }

    }

}