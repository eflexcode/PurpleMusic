package com.larrex.purplemusic

import android.provider.ContactsContract.Data
import android.text.format.Time
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.larrex.purplemusic.ui.theme.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Util {

    companion object {

        val BottomBarBackground
            @Composable get() =
                if (isSystemInDarkTheme()) Color.Black else Color.White

        val BottomBarBackground2
            @Composable get() =
                if (isSystemInDarkTheme()) Color.Gray else Color.White

        val TextColor
            @Composable get() = if (isSystemInDarkTheme()) Color.White else Color.Black

        val BottomBarLabelSelected
            @Composable get() =
                if (isSystemInDarkTheme())
                    Color.White
                else
                    Purple

        val PickSongsFloatingBackground
            @Composable get() =
                if (isSystemInDarkTheme())
                    PurplePickColorDark
                else
                    Color.White

        val BottomBarLabel
            @Composable get() =
                if (isSystemInDarkTheme())
                    Color.Gray
                else
                    Color.Gray

        val pickSongsBackground
            @Composable
            get() = if (isSystemInDarkTheme())
                PurplePickColorDark
            else
                PurplePickSongs

        val searchBarBackground
            @Composable
            get() = if (isSystemInDarkTheme())
                searchBarColorDark
            else
                searchBarColor

        val playlistButtonBackground
            @Composable
            get() = if (isSystemInDarkTheme())
                PlaylistColorDark
            else
                PlaylistColor

        val ChipBackground
            @Composable
            get() = if (isSystemInDarkTheme())
                Color.Black
            else
                Color.White

        val ChipBackgroundSelected
            @Composable
            get() = if (isSystemInDarkTheme())
                searchBarColorDark
            else
                PurpleGray

        fun getGreeting(): String {

//            val calendar: Calendar = //.HOUR_OF_DAY

            val time = Date().hours

            return if (time < 12) {
                "Good morning ✨"
            } else if (time < 16) {
                "Good afternoon ✨"
            } else if (time < 18) {
                "Good evening ✨"
            } else {
                "Good night ✨"
            }

        }

        fun formatTime(duration: String): String {

            return if (duration != "null") {

                val mDuration = duration.toLong()

                var timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

                if (mDuration >= 3600000L){
                    timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                }

                timeFormat.format(Date(mDuration - TimeZone.getDefault().rawOffset))

            } else {

                "00:00"
            }
        }

    }
}