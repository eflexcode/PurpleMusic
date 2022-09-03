package com.larrex.purplemusic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.magnifier
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.larrex.purplemusic.ui.navigation.BottomBarScreens
import com.larrex.purplemusic.ui.navigation.BottomNavGraph
import com.larrex.purplemusic.ui.theme.*

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
                    MainUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi() {

    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = { CreateBNV(navController = navController) }) {
            BottomNavGraph(navController = navController)
        }
    }


}

@Composable
fun CreateBNV(navController: NavHostController) {

    var indexSelected by remember {
        mutableStateOf(0)
    }

//    val current

    val navItems = listOf(
        BottomBarScreens.MusicScreen.barTitle,
        BottomBarScreens.FavouriteScreen.barTitle,
        BottomBarScreens.AlbumScreen.barTitle,
        BottomBarScreens.SearchScreen.barTitle
    )

    val navIcons = listOf(
        R.drawable.ic_music,
        R.drawable.ic_favourite,
        R.drawable.ic_album,
        R.drawable.ic_search
    )

    val navIconsSelected = listOf(
        R.drawable.ic_music_selected,
        R.drawable.ic_favourite_selected,
        R.drawable.ic_album_selected,
        R.drawable.ic_search_selected
    )

    NavigationBar(containerColor = Util.BottomBarBackground) {

        navItems.forEachIndexed() { index, items ->

            NavigationBarItem(selected = indexSelected == index,
                onClick = {
                    indexSelected = index
                    navController.navigate(items.toLowerCase()){
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    PurpleMusicTheme {
        MainUi()
    }

}