package com.compose.wanandroid.ui.page.profile.share

import androidx.compose.foundation.layout.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.compose.wanandroid.ui.widget.StatePage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.widget.RefreshList
import kotlinx.coroutines.launch

fun NavGraphBuilder.mineShareGraph(controller: NavController) {
    composable(route = Page.MineShare.route) {
        MineSharePage(controller)
    }
}

@Composable
fun MineSharePage(
    controller: NavController,
    viewModel: MineShareViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pagingItems = viewModel.pager.collectAsLazyPagingItems()
    val scaffoldState = rememberScaffoldState()

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
        scaffoldState = scaffoldState,
        title = "我的分享",
        onBack = { controller.back() },
    ) {
        StatePage(
            modifier = Modifier.fillMaxSize(),
            state = viewModel.getPageState(pagingItems),
            onRetry = {
//                pagingItems.retry()
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
                                    controller.navigate(Page.Web.route, it)
                                })
                        }
                    }
                })
        }
    }
}