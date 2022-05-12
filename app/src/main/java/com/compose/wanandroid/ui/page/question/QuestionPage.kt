package com.compose.wanandroid.ui.page.question

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.common.ArticleItem
import com.compose.wanandroid.ui.widget.RefreshList
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.widget.StatePage

@Composable
fun QuestionPage(
    navController: NavController,
    padding: PaddingValues = PaddingValues(),
    viewModel: QuestionViewModel = viewModel()
) {
    AppScaffold(
        modifier = Modifier.padding(padding),
        topBar = { AppTitleBar(text = "问答", leadingActions = {}) }
    ) {
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
                                    viewModel.dispatch(QuestionViewAction.Collect(it))
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