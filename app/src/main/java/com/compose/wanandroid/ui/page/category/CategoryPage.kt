package com.compose.wanandroid.ui.page.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.AppBarHeight
import com.compose.wanandroid.ui.widget.TextTabBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPage(index: Int = 0, viewModel: CategoryViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(
            initialPage = index,
        )
        val scope = rememberCoroutineScope()

        val titles = viewModel.titles
        TextTabBar(
            index = pagerState.currentPage,
            titles = titles,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppBarHeight),
            backgroundColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.onPrimary,
            unselectedContentColor = AppTheme.colors.textThird,
            onTabSelected = {
                scope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        )

        HorizontalPager(
            count = titles.size, state = pagerState,
            modifier = Modifier.background(AppTheme.colors.background)
        ) { page ->
            when (page) {
                0 -> StructPage()
                1 -> NavigatePage()
            }
        }
    }
}