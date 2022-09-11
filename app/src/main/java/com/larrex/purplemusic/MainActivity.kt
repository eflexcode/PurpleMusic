package com.larrex.purplemusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.larrex.purplemusic.Util.Companion.BottomBarLabel
import com.larrex.purplemusic.Util.Companion.BottomBarLabelSelected
import com.larrex.purplemusic.di.MusicModule
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.navigation.BottomNavGraph
import com.larrex.purplemusic.ui.screens.component.NowPlayingBar
import com.larrex.purplemusic.ui.theme.*
import com.larrex.purplemusic.ui.viewmodel.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext

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

    var indexSelected by remember {
        mutableStateOf(0)
    }

    var currentIndex by rememberSaveable() {
        mutableStateOf(0)
    }

    val navItems = listOf(
        BottomBarScreens.MusicScreen.barTitle,
        BottomBarScreens.AlbumScreen.barTitle,
        BottomBarScreens.FavouriteScreen.barTitle,
        BottomBarScreens.SearchScreen.barTitle
    )

    val navIcons = listOf(
        R.drawable.ic_music,
        R.drawable.ic_album,
        R.drawable.ic_favourite,
        R.drawable.ic_search
    )

    val navIconsSelected = listOf(
        R.drawable.ic_music_selected,
        R.drawable.ic_album_selected,
        R.drawable.ic_favourite_selected,
        R.drawable.ic_search_selected
    )

    val selectedLabelColor = BottomBarLabelSelected
    val labelColor = BottomBarLabel


    var rememberLabelColor = remember {
        if (indexSelected == currentIndex) selectedLabelColor  else labelColor
    }

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 10.dp), shape = RoundedCornerShape(0.dp)) {

            NowPlayingBar {

            }
        }

        NavigationBar(containerColor = Util.BottomBarBackground, tonalElevation = 40.dp) {

            navItems.forEachIndexed() { index, items ->

                NavigationBarItem(selected = indexSelected == index,
                    onClick = {
                        indexSelected = index
                        currentIndex = index

                        navController.navigate(items.toLowerCase()) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true

                        }
                    },
                    label = {
                        Text(
                            text = items,
                            color = if (indexSelected == index) Util.BottomBarLabelSelected else Util.BottomBarLabel
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (indexSelected == index) navIconsSelected[index] else navIcons[index]),
                            contentDescription = "", modifier = Modifier
                                .padding(0.dp)
                                .size(20.dp),
                            tint = if (indexSelected == index) Util.BottomBarLabelSelected else Util.BottomBarLabel
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