package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.room.Playlist

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayListItem(playlist: Playlist, floating : Boolean, onClicked: () -> Unit,) {

    Row(
        modifier = Modifier
            .background( if (floating) Util.PickSongsFloatingBackground else Util.BottomBarBackground)
            .fillMaxWidth()
            .padding(start = 0.dp)
            .size(65.dp)
            .combinedClickable(
                onClick = {

                    onClicked()


                },

                ), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

      CustomGridImages(images = playlistModel.images, modifier = Modifier.padding(start = 10.dp).size(45.dp).clip(RoundedCornerShape(5.dp)))

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

//            Text(
//                text = "Tracks "+playlistModel.count,
//                fontSize = 12.sp,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                fontStyle = FontStyle.Normal,
//                fontFamily = FontFamily.Default,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.padding(end = 5.dp, start = 5.dp),
//                color = Color.Gray
//            )

        }

    }

}