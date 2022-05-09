package com.compose.wanandroid.ui.page.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.CenterAppBar
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPage(
    navController: NavController,
    padding: PaddingValues = PaddingValues(),
    index: Int = 0,
    viewModel: CategoryViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val pagerState = rememberPagerState(
            initialPage = index,
        )
        val scope = rememberCoroutineScope()
        val titles = viewModel.titles

        CenterAppBar(
            modifier = Modifier.fillMaxWidth(),
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
            backgroundColor = AppTheme.colors.primary
        )

        HorizontalPager(
            count = titles.size, state = pagerState,
            modifier = Modifier.background(AppTheme.colors.background)
        ) { page ->
            when (page) {
                0 -> StructPage(navController)
                1 -> NavigatePage(navController)
            }
        }
    }
}

@ExperimentalPagerApi
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
): Modifier = composed {
    // If there are no pages, nothing to show
    if (pagerState.pageCount == 0) return@composed this

    val targetIndicatorOffset: Dp
    val indicatorWidth: Dp

    val currentTab = tabPositions[minOf(tabPositions.lastIndex, pagerState.currentPage)]
    val targetPage = pagerState.targetPage
    val targetTab = tabPositions.getOrNull(targetPage)

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction = (pagerState.currentPageOffset / max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
        indicatorWidth = lerp(currentTab.width, targetTab.width, fraction).absoluteValue
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
        indicatorWidth = currentTab.width
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = targetIndicatorOffset)
        .width(indicatorWidth)
}

private inline val Dp.absoluteValue: Dp
    get() = value.absoluteValue.dp