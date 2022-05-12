package com.compose.wanandroid.ui.page.struct

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.ArticleItem
import com.compose.wanandroid.ui.common.CollectViewAction
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.showSnackbar
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.widget.RefreshList
import com.compose.wanandroid.ui.widget.StatePage
import kotlinx.coroutines.launch

@Composable
fun CategoryDetailPage(
    navController: NavController,
    viewModel: CategoryDetailViewModel,
    scaffoldState: ScaffoldState
) {
    val viewState = viewModel.viewState
    val pagingItems = viewState.pagingData.collectAsLazyPagingItems()
    val isRefreshing = viewState.isRefreshing
    val listState = if (pagingItems.itemCount > 0) viewState.listState else LazyListState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is SnackViewEvent -> {
                    scope.launch {
                        scaffoldState.showSnackbar(it.message)
                    }
                }
            }
        }
    }

    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewState.getPageState(pagingItems),
        onRetry = {
            pagingItems.retry()
        }
    ) {
        RefreshList(
            lazyPagingItems = pagingItems,
            modifier = Modifier.fillMaxSize(),
            isRefreshing = isRefreshing,
            listState = listState,
            onRefresh = {

            },
            itemContent = {
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