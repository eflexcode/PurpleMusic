package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util


@Preview(showBackground = true)
@Composable
fun Preview() {

    MusicItem {

    }

}

@Composable
fun MusicItem(onClicked: () -> Unit) {
//   val article: Article = Article("","Compose brings a simple and performant way of creating scrolling lists, with fewer lines of code than RecyclerView. Learn how lazy layouts enable adding content on demand, how to use Lazy ","","","https://firebasestorage.googleapis.com/v0/b/liked-a0f31.appspot.com/o/displayImages%2Fgallery%2F1614217903012jpg?alt=media&token=e2b1e9c8-8e6b-4e6e-a8fd-e65221385125","","", Source("",""))
    Row(
        modifier = Modifier
            .background(Util.BottomBarBackground)
            .fillMaxWidth()
            .padding(start = 0.dp)
            .toggleable(value = true, onValueChange = {
                onClicked()
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Card(

            modifier = Modifier
                .padding(top = 5.dp, end = 5.dp, start = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .size(60.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.music_test_image2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
            )


        }

        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = "Crazy things",
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                color = Util.TextColor ,
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

    }
}