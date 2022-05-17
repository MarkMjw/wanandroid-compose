package com.compose.wanandroid.ui.page.profile.collect

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
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
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.widget.ProgressDialog
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

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect { event ->
            when (event) {
                is SnackViewEvent -> {
                    showDialog = false
                    scope.launch {
                        scaffoldState.showSnackbar(message = event.message)
                    }
                }

                is ProgressViewEvent -> showDialog = event.show
            }
        }
    }

    if (showDialog) {
        ProgressDialog("加载中...") {
            showDialog = false
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
                                viewModel.dispatch(CollectViewAction.Collect(it))
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