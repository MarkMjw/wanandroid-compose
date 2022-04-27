package com.compose.wanandroid.ui.page.question

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.ui.common.ArticleItem
import com.compose.wanandroid.ui.common.RefreshList

@Composable
fun QuestionPage(viewModel: QuestionViewModel = viewModel()) {
    val pagingItems = viewModel.pager.collectAsLazyPagingItems()

    RefreshList(modifier = Modifier.fillMaxSize(), lazyPagingItems = pagingItems,
        onRefresh = {

        },
        itemContent = {
            itemsIndexed(pagingItems) { _: Int, value: Article? ->
                if (value != null) {
                    ArticleItem(data = value)
                }
            }
        })
}