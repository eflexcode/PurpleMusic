package com.larrex.purplemusic.ui.screens

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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@Composable
fun AlbumDetailsScreen(albumItem: AlbumItem?, navController: NavController) {

    val painter = rememberAsyncImagePainter(
        model = albumItem?.albumCoverImageUri,
        error = painterResource(id = R.drawable.ic_music_selected_small)
    )

    val viewModel = hiltViewModel<MusicViewModel>()

    val albumName: String = albumItem?.albumName ?: ""

    val songsInAlbum by viewModel.getAllSongsFromAlbum(albumName)
        .collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxSize()
            .padding(bottom = 138.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Util.BottomBarBackground)
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
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier

                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxSize()

                        )
                    }
                    if (albumItem != null) {
                        Text(
                            text = albumItem.albumName,
                            fontSize = 23.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = Util.TextColor,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 20.dp, start = 20.dp, top = 10.dp)
                        )
                    }

                    if (albumItem != null) {
                        Text(
                            text = albumItem.artistName,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(
                                end = 20.dp,
                                start = 20.dp,
                                bottom = 10.dp,
                                top = 5.dp
                            ),
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { },
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

            items(songsInAlbum) {
                MusicItem(onClicked = { }, songItem = it)
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
//    AlbumDetailsScreen()
}