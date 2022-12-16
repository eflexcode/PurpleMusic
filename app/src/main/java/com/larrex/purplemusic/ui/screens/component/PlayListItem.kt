package com.larrex.purplemusic.ui.screens.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.ui.theme.Gray
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "PlayListItem"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayListItem(
    playlist: Playlist,
    floating: Boolean,
    playlists: List<Playlist>,
    viewModel: MusicViewModel,
    onClicked: () -> Unit,
) {

    Log.d(TAG, "PlayListItem: " + playlist.playlistName)
//to get tracks count
    val songs by viewModel.getPlaylistContentWithId(playlist.playlistId)
        .collectAsState(initial = emptyList())

    var showDialog by remember {
        mutableStateOf(false)
    }

    if (showDialog) {
        AlertDialog(dismissButton = {
            Button(
                modifier = Modifier.padding(start = 0.dp),
                onClick = { showDialog = false },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray, containerColor = Util.playlistButtonBackground
                )
            ) {

                Text(text = "Cancel")

            }
        }, onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            Button(
                modifier = Modifier.padding(end = 0.dp),
                onClick = {

                    showDialog = false

                    CoroutineScope(Dispatchers.IO).launch {

                        val ids: MutableList<Int> = ArrayList<Int>()

                        songs.forEach { mSongs->

                            mSongs.id?.let { ids.add(it) }

                        }.also {

                            viewModel.deleteAPlaylist(ids)

                        }

                    }

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, containerColor = Purple
                )
            ) {

                Text(text = "Delete")

            }

        }, title = {
            Text(text = "Delete playlist \"" + playlist.playlistName + "\"")
        }, text = {
            Text(text = "note that this cannot be undone")
        }, containerColor = Util.searchBarBackground)
    }

    Row(
        modifier = Modifier
            .background(if (floating) Util.PickSongsFloatingBackground else Util.BottomBarBackground)
            .fillMaxWidth()
            .padding(start = 0.dp)
            .size(65.dp)
            .combinedClickable(
                onClick = {

                    onClicked()
                },

                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        // believe me this was the only way i would think of

        CustomGridImages(
            playlists,
            modifier = Modifier
                .padding(start = 10.dp)
                .size(45.dp)
                .clip(
                    RoundedCornerShape(5.dp)
                ),
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 15.dp, start = 5.dp)
        ) {

            Text(
                text = playlist.playlistName,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                color = Util.TextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp)
            )

            Text(
                text = "Tracks ${songs.size - 1}",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp),
                color = Color.Gray
            )

        }

        if (!floating)
        IconButton(onClick = { showDialog = true }) {

            Icon(
                painter = painterResource(id = R.drawable.ic_delete_forever),
                contentDescription = "Delete forever",
                tint = Gray
            )

        }

    }

}