package com.larrex.purplemusic.ui.screens.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.larrex.purplemusic.R
import com.larrex.purplemusic.domain.room.Playlist
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

private const val TAG = "CustomGridImages"

@Composable
fun CustomGridImages(images: List<Playlist>, modifier: Modifier) {

    Log.d(TAG, "CustomGridImages: ${images.size}")

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

            } 2 -> {
                Image(
                    painter =
                    rememberAsyncImagePainter(
                        model = images[1].songCoverImageUri,
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
            3 -> {

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = images[1].songCoverImageUri,
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(1f).fillMaxHeight()

                    )

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = images[2].songCoverImageUri,
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(1f).fillMaxHeight()

                    )
                }

            }
            4 -> {

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        rememberAsyncImagePainter(
                            model = images[1].songCoverImageUri,
                            error = painterResource(
                                id = R.drawable.ic_music_selected_small
                            )

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.weight(2f).fillMaxHeight().fillMaxWidth()

                    )

                    Column(modifier = Modifier.weight(2f)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[2].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[3].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                    }


                }

            }
            5 -> {
                Row(modifier = Modifier.fillMaxSize()) {

                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[1].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[2].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[3].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = images[4].songCoverImageUri,
                                error = painterResource(
                                    id = R.drawable.ic_music_selected_small
                                )

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
                        )
                    }


                }
            }

        }

    }
}