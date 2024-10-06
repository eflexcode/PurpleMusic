package com.larrex.purplemusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.larrex.purplemusic.Util.Companion.BottomBarLabel
import com.larrex.purplemusic.Util.Companion.BottomBarLabelSelected
import com.larrex.purplemusic.Util.Companion.searchBarBackground
import com.larrex.purplemusic.domain.exoplayer.service.PlayerService
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.navigation.BottomNavGraph
import com.larrex.purplemusic.ui.screens.component.NowPlayingBar
import com.larrex.purplemusic.ui.theme.*
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, PlayerService::class.java))
        setContent {
            PurpleMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi(application, this)
                }
            }
        }
    }

}

private const val TAG = "MainActivity"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi(application: Application, mainActivity: MainActivity) {

    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val TAG = "MainActivity"

    val navController = rememberNavController()
    val viewModel = hiltViewModel<MusicViewModel>()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Scaffold(bottomBar = { CreateBNV(navController = navController,viewModel) }) {

            BottomNavGraph(navController = navController, viewModel)

        }
    }

}

@Composable
fun CreateBNV(navController: NavHostController, viewModel: MusicViewModel) {

    val navItems = listOf(
        BottomBarScreens.MusicScreen,
        BottomBarScreens.AlbumScreen,
        BottomBarScreens.ArtistScreen,
        BottomBarScreens.FavouriteScreen
    )

    val navIcons = listOf(
        R.drawable.ic_music,
        R.drawable.ic_album,
        R.drawable.ic_artist,
        R.drawable.ic_list,
    )

    val navIconsSelected = listOf(
        R.drawable.ic_music_selected,
        R.drawable.ic_album_selected,
        R.drawable.ic_artist_selected,
        R.drawable.ic_list_selected
    )

    val navBackStackEntry2 by navController.currentBackStackEntryAsState()
    val currentDestination2 = navBackStackEntry2?.destination

    val isNavBarVisible = currentDestination2?.route != BottomBarScreens.NowPlayingScreen.route

//    val viewModel = hiltViewModel<MusicViewModel>()
    val nowPlaying by viewModel.getNowPlaying().collectAsState(null)

    AnimatedVisibility(
        visible = isNavBarVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(searchBarBackground)
                        .height(2.dp)
                )
                if (nowPlaying != null) {
                    Box(
                        modifier = Modifier
                            .background(Purple, RoundedCornerShape(100))
                            .height(2.dp)
                            .fillMaxWidth((viewModel.currentDuration.toFloat() / nowPlaying!!.duration) * 1F)
                    )
                }
            }
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                NowPlayingBar(nowPlaying,viewModel) {

                    navController.navigate(BottomBarScreens.NowPlayingScreen.route)

                }
            }

            NavigationBar(containerColor = Util.BottomBarBackground, tonalElevation = 40.dp) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEachIndexed() { index, items ->

                    var isRout =
                        currentDestination?.hierarchy?.any { items.route == it.route } == true

                    if (items.route == BottomBarScreens.AlbumScreen.route &&
                        currentDestination?.route == BottomBarScreens.AlbumDetailsScreen.route
                    ) {
                        isRout = true
                    }

                    if (items.route == BottomBarScreens.ArtistScreen.route &&
                        currentDestination?.route == BottomBarScreens.ArtistDetailsScreen.route
                    ) {
                        isRout = true
                    }

                    if (items.route == BottomBarScreens.FavouriteScreen.route &&
                        currentDestination?.route == BottomBarScreens.PlaylistDetailsScreen.route
                    ) {
                        isRout = true
                    }

                    NavigationBarItem(selected = isRout,
                        onClick = {

                            navController.navigate(items.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true

                            }
                        },
                        label = {
                            Text(
                                text = items.barTitle,
                                color = if (isRout) BottomBarLabelSelected else BottomBarLabel
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = if (isRout) navIconsSelected[index] else navIcons[index]),
                                contentDescription = "", modifier = Modifier
                                    .padding(0.dp)
                                    .size(20.dp),
                                tint = if (isRout) BottomBarLabelSelected else BottomBarLabel
                            )
                        }
                    )
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    PurpleMusicTheme {
//        MainUi()
    }

}