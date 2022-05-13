package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.TabText
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

fun NavGraphBuilder.collectGraph(controller: NavController) {
    composable(route = Screen.Collect.route) {
        CollectPage(controller)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollectPage(
    controller: NavController,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val titles = listOf(
        TabText(0, "文章"),
        TabText(1, "网址"),
    )

    AppScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppTitleBar(
                onBack = { controller.back() },
                title = {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        backgroundColor = Color.Transparent,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                                    .clip(RoundedCornerShape(2.dp)),
                                height = 3.dp,
                                color = AppTheme.colors.onPrimary
                            )
                        },
                        divider = {},
                        modifier = Modifier
                            .width(130.dp)
                            .fillMaxHeight()
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .fillMaxHeight(),
                                text = {
                                    Text(
                                        text = title.text,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        fontWeight = if (index == pagerState.currentPage) FontWeight.SemiBold else FontWeight.Normal,
                                    )
                                },
                                selectedContentColor = AppTheme.colors.onPrimary,
                                unselectedContentColor = AppTheme.colors.onPrimary.copy(0.7f),
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(index)
                                    }
                                },
                            )
                        }
                    }
                },
            )
        }
    ) {
        HorizontalPager(
            count = titles.size, state = pagerState,
            modifier = Modifier.background(AppTheme.colors.background)
        ) { page ->
            when (page) {
                0 -> CollectArticlePage(controller, scaffoldState)
                1 -> CollectLinkPage(controller, scaffoldState)
            }
        }
    }
}