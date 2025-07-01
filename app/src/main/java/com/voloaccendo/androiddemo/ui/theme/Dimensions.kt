package com.voloaccendo.androiddemo.ui.theme

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val screenPadding: Dp = 6.dp,
    val descriptionHorizontalPadding: Dp = 8.dp,
    val settingSpacer: Dp = 12.dp,
    val switchHorizontalPadding: Dp = 10.dp,
    val listItemPadding: Dp = 12.dp
)

@Composable
fun dynamicDimensions(windowSizeClass: WindowSizeClass): Dimensions {
    return  when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Dimensions(
                screenPadding = 4.dp,
                descriptionHorizontalPadding = 4.dp,
                settingSpacer = 4.dp,
                listItemPadding = 6.dp)
        }
        WindowWidthSizeClass.Medium -> {
            Dimensions(
                screenPadding = 5.dp,
                descriptionHorizontalPadding = 6.dp,
                settingSpacer = 8.dp,
                switchHorizontalPadding = 8.dp,
                listItemPadding = 10.dp)
        }
        else -> {
            Dimensions()
        }
    }
}
