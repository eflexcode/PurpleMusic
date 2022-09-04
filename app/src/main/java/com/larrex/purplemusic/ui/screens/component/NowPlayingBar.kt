package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun NowPlayingBar(onClicked: () -> Unit) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(65.dp)
        .toggleable(value = true, onValueChange = {
            onClicked()
        })
        .background(Util.BottomBarBackground), contentAlignment = Alignment.Center) {


            Row(
                modifier = Modifier
                    .background(Util.BottomBarBackground)
                    .fillMaxWidth()
                    .padding(start = 0.dp)

                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Card(

                    modifier = Modifier
                        .padding(top = 5.dp, end = 5.dp, start = 10.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(0.5.dp))
                        .size(45.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.music_test_image2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(0.5.dp))
                    )


                }

                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        text = "Crazy things",
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        color = Util.TextColor,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 5.dp, start = 5.dp)
                    )

                    Text(
                        text = "Tems",
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

                Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_rewind), contentDescription =null )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_pause), contentDescription =null )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_forward), contentDescription =null )
                    }

                }


            }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNowPlayingBar() {

    NowPlayingBar {

    }

}
