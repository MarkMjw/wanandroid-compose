package com.compose.wanandroid.ui.page.square

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.TabText
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SquareHostPage(
    controller: NavController,
    padding: PaddingValues = PaddingValues(),
) {
    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState(
        initialPage = 0,
    )
    val scope = rememberCoroutineScope()
    val titles = listOf(
        TabText(0, "广场"),
        TabText(1, "问答"),
    )

    AppScaffold(
        modifier = Modifier.padding(padding),
        scaffoldState = scaffoldState,
        topBar = {
            AppTitleBar(
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
                leadingActions = {}
            )
        }
    ) {
        HorizontalPager(
            count = titles.size, state = pagerState,
            modifier = Modifier.background(AppTheme.colors.background)
        ) { page ->
            when (page) {
                0 -> SquarePage(controller, scaffoldState)
                1 -> QuestionPage(controller, scaffoldState)
            }
        }
    }
}
