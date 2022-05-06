package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.defaultContentColorFor

@Composable
fun TextTabBar(
    index: Int,
    titles: List<TabText>,
    modifier: Modifier = Modifier,
    contentAlign: Alignment = Alignment.Center,
    backgroundColor: Color = AppTheme.colors.primary,
    contentColor: Color = defaultContentColorFor(backgroundColor),
    unselectedContentColor: Color = defaultContentColorFor(backgroundColor),
    onTabSelected: (index: Int) -> Unit = { }
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppBarHeight)
            .background(backgroundColor)
            .horizontalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.align(contentAlign)) {
            titles.forEachIndexed { i, title ->
                Text(
                    text = title.text,
                    fontSize = 15.sp,
                    fontWeight = if (index == i) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                        .clickable {
                            onTabSelected(i)
                        },
                    color = if (index == i) contentColor else unselectedContentColor
                )
            }
        }
    }
}

data class TabText(
    val id: Int,
    val text: String,
    var cachePosition: Int = 0,
    var selected: Boolean = false
)