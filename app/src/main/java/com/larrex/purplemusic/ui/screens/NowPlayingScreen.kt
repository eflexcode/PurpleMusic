package com.larrex.purplemusic.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.screens.component.MusicItem
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(navController: NavController) {

    var sliderValue by remember {
        mutableStateOf(60f)
    }

    var newText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val viewModel = hiltViewModel<MusicViewModel>()

    val musicItems by viewModel.getAllSongs().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Util.BottomBarBackground)
    ) {

        LazyColumn() {

            item {

                IconButton(
                    onClick = {

                        viewModel.setBarVisible(true)
                        navController.popBackStack()

                    }, modifier = Modifier
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "cancel"
                    )

                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 70.dp),
                ) {

                    Card(
                        modifier = Modifier
                            .background(Util.BottomBarBackground)
                            .size(height = 360.dp, width = 360.dp)
                            .padding(top = 0.dp, end = 0.dp, start = 0.dp, bottom = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Util.BottomBarBackground),
                        elevation = CardDefaults.cardElevation(10.dp),

                        ) {
                        Image(
                            painter = painterResource(id = R.drawable.legends_never_die),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier

                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxSize()

                        )
                    }

                    Text(
                        text = "Man of the year",
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = Util.TextColor,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(end = 5.dp, start = 5.dp, top = 25.dp)
                    )
                    Text(
                        text = "Juice wrld",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(end = 5.dp, start = 5.dp, top = 5.dp, bottom = 30.dp)
                    )

                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .height(30.dp),
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple,
                            activeTickColor = Purple
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "23:34",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            text = "40:00",
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

                        IconButton(onClick = { /*TODO*/ }, colors = IconButtonDefaults.iconButtonColors(contentColor = Purple)) {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = null
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(70.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_skip_backward),
                                contentDescription = null, modifier = Modifier.size(40.dp)
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(80.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_pause),
                                contentDescription = null, modifier = Modifier.size(70.dp)
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(70.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_skip_forward),
                                contentDescription = null, modifier = Modifier.size(40.dp)
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                            Icon(
                                painter = painterResource(id = R.drawable.repeat_one),
                                contentDescription = null
                            )
                        }
                    }

                    TextField(
                        value = newText,
                        onValueChange = { text ->
                            newText = text
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
                        placeholder = { Text(text = "Search music", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null, modifier = Modifier.size(20.dp),
                            )
                        }

                    )

                }
            }

            items(musicItems) {

                MusicItem(onClicked = {}, it)

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun Pre() {

    val navController = rememberNavController()

    NowPlayingScreen(navController)

}