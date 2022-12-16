package com.larrex.purplemusic.ui.screens.component

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.ui.theme.Gray
import com.larrex.purplemusic.ui.theme.PurpleGray
import com.larrex.purplemusic.ui.theme.PurplePickSongs
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicItemPlaylist(
    onClicked: () -> Unit,
    songItem: Playlist,
    viewModel: MusicViewModel
) {

    val painter = rememberAsyncImagePainter(
        model = songItem.songCoverImageUri,
        error = painterResource(
            id = R.drawable.ic_music_selected_small
        )
    )

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxWidth()
            .padding(start = 0.dp)
            .size(65.dp)
            .combinedClickable(
                onClick = {

                    onClicked()

                }
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
                text = songItem.artistName,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp),
                color = Color.Gray
            )

        }

        IconButton(onClick = {
            Toast.makeText(context, "Successfully deleted ${songItem.playlistName}", Toast.LENGTH_LONG).show()
            CoroutineScope(Dispatchers.IO).launch {

                songItem.id?.let { viewModel.deleteSingleItemFromAPlaylist(it) }

            }


        }) {

            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "Delete forever",
                tint = Gray
            )

        }

    }
}