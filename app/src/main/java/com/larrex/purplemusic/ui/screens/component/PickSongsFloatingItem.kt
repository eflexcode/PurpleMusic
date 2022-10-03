package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util

@Composable
fun PickSongsFloatingItem(count : Int, play:() -> Unit) {

    Surface(shadowElevation = 5.dp, shape = RoundedCornerShape(10.dp), modifier = Modifier) {

        Box(
            modifier = Modifier.background(
                Util.PickSongsFloatingBackground,
                ).padding(10.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Long press to select",
                    fontSize = 8.sp,
                    color = Util.TextColor
                )

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                    Text(
                        text = "$count Selected",
                        color = Util.TextColor,
                        fontSize = 15.sp,
                    )

                    IconButton(onClick = { play()}, modifier = Modifier.size(20.dp)) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_play),
                            contentDescription = null
                        )
                    }

                }

            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun PickSongsFloatingItemPreview() {

    PickSongsFloatingItem(45){}

}