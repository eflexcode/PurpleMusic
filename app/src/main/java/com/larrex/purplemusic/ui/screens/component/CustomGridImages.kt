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
import coil.compose.rememberAsyncImagePainter
import com.larrex.purplemusic.R

@Composable
fun CustomGridImages(images: List<String>, modifier: Modifier) {


    Box(modifier = modifier) {

        when (images.size) {

            1 -> {
                Image(
                    painter =
                    rememberAsyncImagePainter(
                        model = images[0],
                        error = painterResource(
                            id = R.drawable.ic_music_selected_small
                        )

                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()

                )

            }
            2 -> {

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter =rememberAsyncImagePainter(
                            model = images[0],
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(1f)

                    )

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = images[1],
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
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
                        rememberAsyncImagePainter(
                            model = images[0],
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(2f)

                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[1],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[2],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
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
                            painter = rememberAsyncImagePainter(
                                model = images[0],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[1],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[2],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[3],
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
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