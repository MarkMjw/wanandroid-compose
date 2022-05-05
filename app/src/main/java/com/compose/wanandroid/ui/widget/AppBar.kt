package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

val AppBarHeight = 48.dp
private val AppBarHorizontalPadding = 4.dp
private val ContentPadding = PaddingValues(
    start = AppBarHorizontalPadding,
    end = AppBarHorizontalPadding
)

@Composable
fun CenterAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues = ContentPadding,
    navController: NavController? = null,
    leftActions: @Composable (RowScope.() -> Unit)? = null,
    rightActions: @Composable (RowScope.() -> Unit) = { },
    title: @Composable () -> Unit,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier
    ) {
        var leftSectionWidth by remember { mutableStateOf(0.dp) }
        var rightSectionWidth by remember { mutableStateOf(0.dp) }
        var titlePadding by remember { mutableStateOf(PaddingValues()) }

        val calculateTitlePadding = fun() {
            val dx = leftSectionWidth - rightSectionWidth
            var start = 0.dp
            var end = 0.dp
            if (dx < 0.dp) start += -dx else end += dx
            titlePadding = PaddingValues(start = start, end = end)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // left
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                with(LocalDensity.current) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .onGloballyPositioned { coordinates ->
                                val width = coordinates.size.width.toDp()
                                if (width != leftSectionWidth) {
                                    leftSectionWidth = width
                                    calculateTitlePadding()
                                }
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        content = leftActions ?: {
                            val previous = navController?.previousBackStackEntry
                            if (previous != null) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, null)
                                }
                            }
                        }
                    )
                }
            }

            // title
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(titlePadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                        content = title
                    )
                }
            }

            // right
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                with(LocalDensity.current) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .onGloballyPositioned { coordinates ->
                                val width = coordinates.size.width.toDp()
                                if (width != rightSectionWidth) {
                                    rightSectionWidth = width
                                    calculateTitlePadding()
                                }
                            },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        content = rightActions
                    )
                }
            }
        }
    }
}