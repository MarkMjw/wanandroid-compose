package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.compose.wanandroid.ui.widget.StatePage
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.launch

@Composable
fun CollectArticlePage(
    controller: NavController,
    scaffoldState: ScaffoldState,
    viewModel: CollectArticleViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pagingItems = viewModel.pager.collectAsLazyPagingItems()

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

    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.getPageState(pagingItems),
        onRetry = {
            pagingItems.retry()
        }
    ) {
        RefreshList(
            lazyPagingItems = pagingItems,
            modifier = Modifier.fillMaxSize(),
            itemContent = {
                itemsIndexed(pagingItems) { _: Int, value: Article? ->
                    if (value != null) {
                        ArticleItem(data = value,
                            onCollectClick = {
                                viewModel.dispatch(CollectViewAction.UnCollect(it))
                            },
                            onUserClick = { id ->
                                "用户:$id".toast(context)
                            },
                            onSelected = {
                                controller.navigate(Screen.Web.route, it)
                            })
                    }
                }
            })
    }
}