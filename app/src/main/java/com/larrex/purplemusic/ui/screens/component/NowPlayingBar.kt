package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.nowplayingroom.NowPlaying

@Composable
fun NowPlayingBar(nowPlaying: NowPlaying?, onClicked: () -> Unit) {

    var sliderValue by remember {
        mutableStateOf(60f)
    }
    val painter = rememberAsyncImagePainter(
        model = nowPlaying?.albumArt, placeholder = painterResource(
            id = R.drawable.ic_music_selected_small
        ), error = painterResource(id = R.drawable.ic_music_selected_small)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = true, onValueChange = {
                onClicked()
            })
            .background(Util.BottomBarBackground), contentAlignment = Alignment.BottomCenter
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

            Row(
                modifier = Modifier
                    .background(Util.BottomBarBackground)
                    .fillMaxWidth()
                    .padding(start = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 5.dp, end = 5.dp, start = 15.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .size(45.dp)
                )

                Column(modifier = Modifier.weight(2f)) {
                    if (nowPlaying != null) {

                        Text(
                            text = nowPlaying.musicName,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            color = Util.TextColor,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 5.dp, start = 5.dp)
                        )

                        Text(
                            text = nowPlaying.artistName,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 5.dp, start = 5.dp),
                            color = Color.Gray
                        )

                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_skip_backward),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_play),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_skip_forward),
                            contentDescription = null
                        )
                    }

                }

            }

//            Slider(
//                value = sliderValue,
//                onValueChange = { sliderValue = it },
//                modifier = Modifier
//                    .padding(start = 0.dp, end = 0.dp)
//                    .height(5.dp),
//                valueRange = 0f..100f,
//                colors = SliderDefaults.colors(
//                    thumbColor = Color.Transparent,
//                    activeTickColor = Purple,
//                    disabledThumbColor = Color.Transparent
//                ),
//                enabled = true
//            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNowPlayingBar() {

//    NowPlayingBar {
//
//    }

}
