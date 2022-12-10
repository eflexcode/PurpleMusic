package com.larrex.purplemusic.ui.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomGridImages(images: List<Int>, modifier: Modifier) {


    Box(modifier =  modifier) {

        when (images.size) {

            1 -> {
                Image(
                    painter = painterResource(id = images[0]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()

                )

            }
            2 -> {

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = images[0]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(1f)

                    )

                    Image(
                        painter = painterResource(id = images[1]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(1f)

                    )
                }

            }
            3 -> {

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = images[0]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(2f)

                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = painterResource(id = images[1]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(id = images[2]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }


                }

            }
            4 -> {
                Row(modifier = Modifier.fillMaxSize()) {

                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = painterResource(id = images[0]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(id = images[1]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = painterResource(id = images[2]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(id = images[3]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }


                }
            }

        }

    }
}