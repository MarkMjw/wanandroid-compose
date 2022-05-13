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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.fromJson
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.page.main.Screen
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoryGraph(controller: NavController) {
    composable(
        route = Screen.CategoryDetail.route + "/{category}/{index}",
        arguments = listOf(
            navArgument("category") { type = NavType.StringType },
            navArgument("index") { type = NavType.IntType }
        )
    ) {
        val args = it.arguments?.getString("category")?.fromJson<Struct>()
        val index = it.arguments?.getInt("index", 0) ?: 0
        if (args != null) {
            CategoryListPage(struct = args, index = index, controller = controller)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryListPage(
    struct: Struct,
    index: Int = 0,
    controller: NavController,
    viewModel: CategoryListViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    AppScaffold(
        topBar = { AppTitleBar(text = struct.name, onBack = { controller.back() }, elevation = 0.dp) },
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
                CategoryDetailPage(controller, viewModel.getChildViewModel(structs[page].id), scaffoldState)
            }
        }
    }
}