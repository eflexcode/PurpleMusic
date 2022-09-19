package com.larrex.purplemusic.ui.screens

import android.Manifest
import android.os.Build
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
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(navController: NavController) {

    val chipItems = listOf("Music", "Albums")
    var newText by remember { mutableStateOf(TextFieldValue("")) }

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

                    Text(text = "Give Permission")

                }

            }

        } else {

            val musicItems by viewModel.getAllSongs().collectAsState(initial = emptyList())

            LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                item {

//                    Row(
//                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//
//                        Text(
//                            text = Util.getGreeting(),
//                            color = Util.TextColor,
//                            fontSize = 25.sp,
//                            fontWeight = FontWeight.Normal,
//                            textAlign = TextAlign.Start, modifier = Modifier
//                                .weight(2f)
//                                .fillMaxWidth()
//                                .padding(start = 15.dp)
//                        )
//
//                        IconButton(
//                            onClick = {
////
////                                navController.navigate(BottomBarScreens.ArtistScreen.route) {
////
////                                    popUpTo(navController.graph.findStartDestination().id) {
////                                        saveState = true
////                                    }
////
////                                    launchSingleTop = true
////                                    restoreState = true
////
////                                }
//
//                            },
//                            modifier = Modifier
//                                .size(18.dp)
//                                .padding(end = 10.dp)
//                                .weight(0.3f)
//                        ) {
//
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_search),
//                                contentDescription = null
//                            )
//
//                        }
//                    }

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

                    }else  {
                        CircularProgressIndicator()
                    }

                }

                items(musicItems) {

                    MusicItem(onClicked = {}, it)

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