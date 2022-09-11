package com.larrex.purplemusic

import android.text.format.Time
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.larrex.purplemusic.ui.theme.Gray
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.theme.PurpleGray
import java.util.*

class Util {

    companion object {

        val BottomBarBackground
            @Composable get() =
                if (isSystemInDarkTheme()) Color.Black else Color.White

        val TextColor
            @Composable get() = if (isSystemInDarkTheme()) Color.White else Color.Black

        val BottomBarLabelSelected
            @Composable get() =
                if (isSystemInDarkTheme())
                    Color.White
                else
                    Purple

        val BottomBarLabel
            @Composable get() =
                if (isSystemInDarkTheme())
                    Color.Gray
                else
                    Color.Gray

        fun getGreeting(): String {

//            val calendar: Calendar = //.HOUR_OF_DAY

            val time = Date().hours

            return if (time < 12) {
                "Good morning"
            }else if (time < 16){
                "Good afternoon"
            }else if (time < 17){
                "Good evening"
            }else{
                "Good night"
            }


        }


    }

}