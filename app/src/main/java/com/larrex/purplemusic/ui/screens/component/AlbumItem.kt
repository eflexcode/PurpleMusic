package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util

@Composable
fun AlbumItem(onClicked: () -> Unit) {

    Column(
        modifier = Modifier
            .background(Util.BottomBarBackground)

            .padding(start = 0.dp)
            .toggleable(value = true, onValueChange = {
                onClicked()
            }),
        horizontalAlignment = Alignment.Start

    ) {

        Card(

            modifier = Modifier
                .padding(top = 5.dp, end = 5.dp, start = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .size(130.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.made_in_lagos),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)).size(130.dp)
            )

        }

            Text(
                text = "Made in Lagos",
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                color = Util.TextColor ,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp)
            )

            Text(
                text = "Wizkid",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 5.dp, start = 5.dp, bottom = 5.dp),
                color = Color.Gray
            )




    }

}

@Preview(showBackground = true)
@Composable
fun PreviewAlbumItem() {
    AlbumItem(){

    }
}