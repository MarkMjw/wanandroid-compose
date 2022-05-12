package com.compose.wanandroid.ui.page.struct

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryListPage(
    struct: Struct,
    index: Int = 0,
    navController: NavController,
    viewModel: CategoryListViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    AppScaffold(
        topBar = { AppTitleBar(text = struct.name, onBack = { navController.back() }, elevation = 0.dp) },
        scaffoldState = scaffoldState
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val pagerState = rememberPagerState(
                initialPage = index,
            )
            val scope = rememberCoroutineScope()
            val structs = struct.children

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = AppTheme.colors.primary,
                edgePadding = 0.dp,
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
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                structs.forEachIndexed { index, struct ->
                    Tab(
                        modifier = Modifier.fillMaxHeight(),
                        text = {
                            Text(
                                text = struct.name,
                                fontSize = 13.sp,
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

            HorizontalPager(
                count = structs.size,
                state = pagerState,
                modifier = Modifier.background(AppTheme.colors.background)
            ) { page ->
                // Pager子页无法区分ViewModel，父页面统一创建和管理
                CategoryDetailPage(navController, viewModel.getChildViewModel(structs[page].id), scaffoldState)
            }
        }
    }
}