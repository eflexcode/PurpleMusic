package com.larrex.purplemusic.ui.screens.component

import android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.theme.PurpleGray
import com.larrex.purplemusic.ui.theme.searchBarColor
import com.larrex.purplemusic.ui.theme.searchBarColorDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylist(onClick:(Boolean) -> Unit) {
    var newText by remember { mutableStateOf(TextFieldValue("")) }

    val dialogState = remember {
        mutableStateOf(false)
    }

//    val state = remember

    Dialog(onDismissRequest = { }) {

        Box(
            modifier = Modifier.background(Util.searchBarBackground, RoundedCornerShape(20.dp))

        ) {

            Column() {

                Text(
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    text = "Create New Playlist",
                    color = Util.TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    value = newText,
                    onValueChange = { text ->
                        newText = text
                    },
                    modifier = Modifier
                        .padding(
                            top = 15.dp, end = 20.dp, start = 20.dp, bottom = 15.dp
                        )
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        placeholderColor = Color.Gray,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    placeholder = { Text(text = "Type playlist name", color = Color.Gray) },

                    )

                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        modifier = Modifier.padding(end = 0.dp),
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White, containerColor = Purple
                        )
                    ) {

                        Text(text = "Create")

                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    Button(
                        modifier = Modifier.padding(start = 0.dp),
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Gray, containerColor = Util.playlistButtonBackground
                        )
                    ) {

                        Text(text = "Dismiss")

                    }

                }

            }


        }

    }

}

@Preview()
@Composable
fun F() {
    CreatePlaylist(){

    }
}