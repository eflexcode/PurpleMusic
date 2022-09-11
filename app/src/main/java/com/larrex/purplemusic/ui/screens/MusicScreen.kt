package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MusicScreen(application: Application) {

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 147.dp)
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

                    Text(text = "Give Permission")

                }

            }

        } else {
            val musicItems by viewModel.getAllSongs().collectAsState(initial = emptyList())

            LazyColumn() {

                item {
                    Text(
                        text = Util.getGreeting(),
                        color = Util.TextColor,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(6.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )


                }

                items(musicItems) {

                    MusicItem(onClicked = {}, it)


                }

            }
        }

    }
}





