package com.larrex.purplemusic.ui.screens

import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.exoplayer2.MediaItem
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "NowPlayingScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(navController: NavController, viewModel: MusicViewModel) {

    var sliderValue by remember {
        mutableStateOf(60f)
    }

    var newText by remember {
        mutableStateOf(TextFieldValue(""))
    }

//    val viewModel = hiltViewModel<MusicViewModel>()

    val nowPlaying by viewModel.getNowPlaying().collectAsState(null)

    val nextUps by viewModel.getNextUps().collectAsState(emptyList())

    val painter = rememberAsyncImagePainter(
        model = nowPlaying?.albumArt, placeholder = painterResource(
            id = R.drawable.ic_music_selected_small
        ), error = painterResource(id = R.drawable.ic_music_selected_small)
    )

    val scrollState = rememberScrollState()
    var animate by remember {
        mutableStateOf(true)
    }

//    do {
//        LaunchedEffect(Unit) {
//            scrollState.animateScrollTo(
//                scrollState.maxValue,
//                animationSpec = tween(10000, 200, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
//            )
//            animate = !animate
//            scrollState.scrollTo(0)
//        }
//    } while (animate)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Util.BottomBarBackground)
    ) {

        LazyColumn() {

            item {

                Row() {

                    IconButton(
                        onClick = {

                            navController.popBackStack()

                        }, modifier = Modifier.weight(0.2f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_down),
                            contentDescription = "cancel"
                        )

                    }

                    Column(
                        modifier = Modifier
                            .padding(end = 35.dp)
                            .fillMaxWidth()
                            .weight(2f)
                    ) {
                        nowPlaying?.playingFromType?.let {
                            Text(
                                text = "Playing From $it",
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 13.dp)
                            )
                        }

                        nowPlaying?.let {
                            Text(
                                text = it.playingFromName,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                textAlign = TextAlign.Center,
                                color = Util.TextColor,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 0.dp, bottom = 0.dp)
                            )
                        }
                    }

                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                ) {

                    Card(
                        modifier = Modifier
                            .background(Util.BottomBarBackground)
                            .size(height = 320.dp, width = 320.dp)
                            .padding(top = 0.dp, end = 0.dp, start = 0.dp, bottom = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Util.BottomBarBackground),
                        elevation = CardDefaults.cardElevation(10.dp),

                        ) {

                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxSize()
                        )

                    }

                    nowPlaying?.musicName?.let {
                        Text(
                            text = it,
                            fontSize = 24.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            color = Util.TextColor,
                            overflow = TextOverflow.Companion.Ellipsis,
                            modifier = Modifier
                                .padding(end = 35.dp, start = 35.dp, top = 25.dp)
                        )
                    }

                    nowPlaying?.let {
                        Text(
                            text = it.artistName,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = 35.dp, start = 35.dp, top = 5.dp, bottom = 10.dp)
                        )
                    }

                    if (nowPlaying != null) {

                        var duration = nowPlaying!!.duration

                        Slider(
                            value = ((viewModel.currentDuration.toFloat() / nowPlaying!!.duration) * 100F),
                            onValueChange = {
                                viewModel.seekToPosition(((it.toLong() * nowPlaying!!.duration.toLong()) / 100L))
                            },
                            modifier = Modifier
                                .padding(start = 30.dp, end = 30.dp),
                            valueRange = 1f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = Purple,
                                activeTickColor = Purple
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 37.dp, end = 37.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = Util.formatTime(viewModel.currentDuration.toFloat()),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            text = Util.formatTime(nowPlaying?.duration?.toFloat()),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            textAlign = TextAlign.End,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                nowPlaying?.id?.let {
                                    viewModel.shuffle(
                                        it,
                                        !nowPlaying!!.shuffle
                                    )
                                }
                            },
                            colors = if (nowPlaying?.shuffle == true) IconButtonDefaults.iconButtonColors(
                                contentColor = Purple
                            ) else IconButtonDefaults.iconButtonColors(contentColor = Util.TextColor)

                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = null
                            )
                        }

                        IconButton(
                            onClick = { viewModel.previous() },
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_skip_backward),
                                contentDescription = null, modifier = Modifier.size(40.dp)
                            )
                        }

                        IconButton(
                            onClick = {

                                nowPlaying?.musicUri?.let {
                                    MediaItem.fromUri(
                                        it
                                    )
                                }?.let { }

                                viewModel.playOrPause()

                            },
                            modifier = Modifier.size(80.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (!viewModel.isPlaying) R.drawable.ic_round_play else R.drawable.ic_round_pause),
                                contentDescription = null, modifier = Modifier.size(70.dp)
                            )
                        }

//                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(80.dp)) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_round_pause),
//                                contentDescription = null, modifier = Modifier.size(70.dp)
//                            )
//                        }

                        IconButton(onClick = {
                            viewModel.next()
                        }, modifier = Modifier.size(70.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_skip_forward),
                                contentDescription = null, modifier = Modifier.size(40.dp)
                            )
                        }

                        IconButton(onClick = {

                            if (nowPlaying != null)
                                nowPlaying!!.id?.let { viewModel.repeat(it, nowPlaying?.repeat!!) }

                        }, modifier = Modifier) {
                            Icon(
                                painter = if (nowPlaying?.repeat == 3) painterResource(id = R.drawable.repeat_one) else painterResource(
                                    id = R.drawable.repeat_all
                                ),
                                contentDescription = null,
                                tint = if (nowPlaying?.repeat == 1) Util.TextColor else Purple
                            )
                        }
                    }

                    TextField(
                        value = newText,
                        onValueChange = { text ->
                            newText = text
                            viewModel.searchNextUps(newText.text.toString())
                        },
                        modifier = Modifier
                            .padding(top = 80.dp, end = 20.dp, start = 20.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            contentColorFor(backgroundColor = Color.Transparent),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Util.searchBarBackground,
                            placeholderColor = Color.Gray,
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text(text = "Search next ups", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null, modifier = Modifier.size(20.dp),
                            )
                        }

                    )

                }
            }

            itemsIndexed(
                if (newText.text.trim().toString()
                        .isEmpty()
                ) nextUps else viewModel.searchNextUpList
            ) { index, item ->

                val songUri: Uri = Uri.parse(item.songUri)
                val imageUri: Uri = Uri.parse(item.songCoverImageUri)

                val songItem =
                    SongItem(
                        songUri,
                        item.songName,
                        item.artistName,
                        imageUri,
                        item.size,
                        item.duration
                    )

                MusicItem(onClicked = {

                    CoroutineScope(Dispatchers.IO).launch {

                        if (nowPlaying != null) {
                            nowPlaying!!.id?.let { it1 ->
                                viewModel.updateNowPlaying(
                                    it1, item.songUri.toString(),
                                    item.songName, item.artistName,
                                    item.songCoverImageUri.toString(),
                                    item.duration
                                )
                            }

                        }
                    }

                    viewModel.jumpToPosition(index)
                    viewModel.play()

                }, onLongClicked = {}, onUnselected = {}, songItem, true)

            }

        }

    }
    LaunchedEffect(Unit) {

        scrollState.animateScrollTo(
            scrollState.maxValue,
            animationSpec = tween(10000, 1000, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
        )

//        scrollState.scrollTo(0)
        animate = !animate
        Log.d(TAG, "NowPlayingScreen: llllllll")
    }
}

@Preview(showBackground = true)
@Composable
fun Pre() {

    val navController = rememberNavController()

//    NowPlayingScreen(navController, viewModel)

}