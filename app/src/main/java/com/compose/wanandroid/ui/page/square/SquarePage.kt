package com.compose.wanandroid.ui.page.square

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.logic.UserStore
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.RefreshList
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.widget.ProgressDialog
import com.compose.wanandroid.ui.widget.StatePage
import kotlinx.coroutines.launch

@Composable
fun SquarePage(
    controller: NavController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: SquareViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val isLogin = UserStore.isLogin.collectAsState(initial = false)
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

    val context = LocalContext.current
    val pagingItems = viewModel.pager.collectAsLazyPagingItems()
    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.getPageState(pagingItems),
        onRetry = {
            pagingItems.retry()
        }
    ) {
        RefreshList(
            lazyPagingItems = pagingItems,
            modifier = Modifier
                .fillMaxSize(),
            itemContent = {
                itemsIndexed(pagingItems) { _: Int, value: Article? ->
                    if (value != null) {
                        ArticleItem(data = value,
                            onCollectClick = {
                                if (isLogin.value) {
                                    viewModel.dispatch(CollectViewAction.Collect(it))
                                } else {
                                    controller.navigate(Page.Login.route)
                                }
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