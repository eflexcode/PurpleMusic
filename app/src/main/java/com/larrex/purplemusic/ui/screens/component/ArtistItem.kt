package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.larrex.purplemusic.domain.model.ArtistItemModel

@Composable
fun ArtistItem(artistItemModel : ArtistItemModel, onClicked: () -> Unit) {

    val painter = rememberAsyncImagePainter(
        model = artistItemModel.coverImageUri,
        error = painterResource(
            id = R.drawable.ic_music_selected_small
        )
    )

        Column(
            modifier = Modifier
                .background(Util.BottomBarBackground)
                .padding(start = 10.dp, end = 10.dp)
                .toggleable(value = true, onValueChange = {
                    onClicked()
                }),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Card(modifier = Modifier
                .background(Util.BottomBarBackground)
                .padding(top = 5.dp, end = 5.dp, start = 5.dp, bottom = 5.dp),
                colors = CardDefaults.cardColors(containerColor = Util.BottomBarBackground),
                elevation = CardDefaults.cardElevation(10.dp)) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier

                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .size(160.dp)
                )
            }
            Text(
                text = artistItemModel.artistName,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                color = Util.TextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp, top = 10.dp)
            )

            Text(
                text = "Albums "+artistItemModel.albumsCount+" • Tracks "+artistItemModel.songsCount,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp, bottom = 10.dp),
                color = Color.Gray
            )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtistItem() {

//    val artistItem = ArtistItem("","",)
//
//    ArtistItem(artistItem) {
//
//    }
}