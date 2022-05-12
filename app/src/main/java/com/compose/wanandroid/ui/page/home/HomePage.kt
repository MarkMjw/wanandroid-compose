package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.*
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    navController: NavController,
    padding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = viewModel()
) {
    val viewState = viewModel.viewState
    val pagingItems = viewState.pagingData.collectAsLazyPagingItems()
    val banners = viewState.banners
    val tops = viewState.tops
    val isRefreshing = viewState.isRefreshing
    val listState = if (pagingItems.itemCount > 0) viewState.listState else LazyListState()
    val scope = rememberCoroutineScope()

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is SnackViewEvent -> {
                    scope.launch {
                        scaffoldState.showSnackbar(message = it.message)
                    }
                }
            }
        }
    }

    AppScaffold(
        modifier = Modifier.padding(padding),
        scaffoldState = scaffoldState,
        topBar = {
            AppTitleBar(
                modifier = Modifier.fillMaxWidth(),
                text = "首页",
                leadingActions = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_scan),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(AppBarHeight)
                            .clickable {
                                // TODO 扫描
                            },
                        colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )
                },
                trailingActions = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(AppBarHeight)
                            .clickable {
                                // TODO 搜索
                            },
                        colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )
                }
            )
        }
    ) { innerPadding ->
        val context = LocalContext.current
        StatePage(
            modifier = Modifier.fillMaxSize(),
            state = viewState.getPageState(pagingItems),
            onRetry = {
                viewModel.dispatch(RefreshViewAction.Refresh)
                pagingItems.retry()
            }
        ) {
            RefreshList(
                lazyPagingItems = pagingItems,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                isRefreshing = isRefreshing,
                listState = listState,
                onRefresh = {
                    viewModel.dispatch(RefreshViewAction.Refresh)
                },
                itemContent = {
                    if (banners.isNotEmpty()) {
                        item {
                            Banner(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.78f),
                                count = banners.size,
                            ) { page ->
                                AsyncImage(
                                    model = banners[page].imagePath,
                                    placeholder = painterResource(id = R.drawable.image_holder),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(AppTheme.colors.secondaryBackground)
                                        .clickable {
                                            with(banners[page]) {
                                                navController.navigate(Screen.Web.route, Link(title = title, url = url))
                                            }
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    if (tops.isNotEmpty()) {
                        itemsIndexed(tops) { index: Int, value: Article? ->
                            if (value != null) {
                                ArticleItem(
                                    data = value,
                                    isTop = true,
                                    modifier = Modifier.padding(top = if (index == 0) 5.dp else 0.dp),
                                    onCollectClick = {
                                        viewModel.dispatch(CollectViewAction.Collect(it))
                                    },
                                    onUserClick = { id ->
                                        "用户:$id".toast(context)
                                    },
                                    onSelected = {
                                        navController.navigate(Screen.Web.route, it)
                                    }
                                )
                            }
                        }
                    }

                    itemsIndexed(pagingItems) { _: Int, value: Article? ->
                        if (value != null) {
                            ArticleItem(
                                data = value,
                                onCollectClick = {
                                    viewModel.dispatch(CollectViewAction.Collect(it))
                                },
                                onUserClick = { id ->
                                    "用户:$id".toast(context)
                                },
                                onSelected = {
                                    navController.navigate(Screen.Web.route, it)
                                })
                        }
                    }
                })
        }
    }
}