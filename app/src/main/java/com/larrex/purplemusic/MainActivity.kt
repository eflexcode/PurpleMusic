package com.larrex.purplemusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.larrex.purplemusic.Util.Companion.BottomBarLabel
import com.larrex.purplemusic.Util.Companion.BottomBarLabelSelected
import com.larrex.purplemusic.Util.Companion.searchBarBackground
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.navigation.BottomNavGraph
import com.larrex.purplemusic.ui.screens.NowPlayingScreen
import com.larrex.purplemusic.ui.screens.component.NowPlayingBar
import com.larrex.purplemusic.ui.theme.*
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PurpleMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi(application)
                }
            }
        }
    }


}

private const val TAG = "MainActivity"


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi(application: Application) {
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val TAG = "MainActivity"

    val navController = rememberNavController()
    val viewModel = hiltViewModel<MusicViewModel>()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Scaffold(bottomBar = { CreateBNV(navController = navController) }) {

            BottomNavGraph(navController = navController, application)

        }
    }

}

@Composable
fun CreateBNV(navController: NavHostController) {

//    val navControllerNowPlaying = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "nowPlaying") {
//
//        composable("nowPlaying"){
//            NowPlayingScreen()
//        }
//
//    }

    var indexSelected by remember {
        mutableStateOf(0)
    }

    var sliderValue by remember {
        mutableStateOf(60f)
    }

    var currentIndex by rememberSaveable() {
        mutableStateOf(0)
    }

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
        R.drawable.ic_favourite,
    )

    val navIconsSelected = listOf(
        R.drawable.ic_music_selected,
        R.drawable.ic_album_selected,
        R.drawable.ic_artist_selected,
        R.drawable.ic_favourite_selected
    )

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
            Box(
                modifier = Modifier
                    .background(Purple, RoundedCornerShape(100))
                    .height(2.dp)
                    .fillMaxWidth(0.3f)
            )

        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            NowPlayingBar {

                navController.navigate("nowPlaying")

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    PurpleMusicTheme {
//        MainUi()
    }

}