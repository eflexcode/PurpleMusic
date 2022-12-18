package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import coil.compose.rememberImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.ui.theme.PurpleGray
import com.larrex.purplemusic.ui.theme.PurplePickSongs
import java.io.File

@Preview(showBackground = true)
@Composable
fun Preview() {

//    MusicItem {
//
//    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicItem(
    onClicked: (state: Boolean) -> Unit,
    onLongClicked: () -> Unit,
    onUnselected: (name: String) -> Unit,
    songItem: SongItem,
    nowPlaying: Boolean
) {

    val painter = rememberAsyncImagePainter(
        model = songItem.songCoverImageUri,
        error = painterResource(
            id = R.drawable.ic_music_selected_small
        )
    )

    var isSelected by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .background(if (!isSelected) Util.BottomBarBackground else Util.pickSongsBackground)
            .fillMaxWidth()
            .padding(start = 0.dp)
            .size(65.dp)
            .combinedClickable(
                onClick = {

                    onClicked(false)
                },
                onLongClick = {

                    if (!isSelected) {

                        onLongClicked()
                        isSelected = true

                    } else {
                        isSelected = false
                        onUnselected(songItem.songName)
//                        onLongClicked()
                    }
                },
                onDoubleClick = { },
            ), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 7.dp, end = 5.dp, start = 15.dp, bottom = 7.dp)
                .clip(RoundedCornerShape(3.dp))
                .size(45.dp)
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 15.dp, start = 5.dp)
        ) {
            Text(
                text = songItem.songName,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                color = Util.TextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp)
            )

            Text(
                text = if (nowPlaying) Util.formatTime(songItem.duration.toString())+" • "+ songItem.artistName else songItem.songName,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp),
                color = Color.Gray
            )

        }

    }
}