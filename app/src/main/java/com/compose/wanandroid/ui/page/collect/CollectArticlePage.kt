package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
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
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.widget.RefreshList

@Composable
fun CollectArticlePage(
    navController: NavController,
    viewModel: CollectArticleViewModel = viewModel()
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
            modifier = Modifier.fillMaxSize(),
            itemContent = {
                itemsIndexed(pagingItems) { _: Int, value: Article? ->
                    if (value != null) {
                        ArticleItem(data = value,
                            onCollectClick = { id ->
                                "收藏:$id".toast(context)
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