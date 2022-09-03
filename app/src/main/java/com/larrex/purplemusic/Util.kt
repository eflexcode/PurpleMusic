package com.larrex.purplemusic

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.larrex.purplemusic.ui.theme.Gray
import com.larrex.purplemusic.ui.theme.Purple
import com.larrex.purplemusic.ui.theme.PurpleGray

class Util {

    companion object {

        val BottomBarBackground
            @Composable get() =
                if (isSystemInDarkTheme()) Color.Black else Color.White

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

    }

}