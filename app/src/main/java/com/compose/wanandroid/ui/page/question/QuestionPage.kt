package com.compose.wanandroid.ui.page.question

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.ui.common.ArticleItem
import com.compose.wanandroid.ui.common.RefreshList
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.defaultContentColorFor
import com.compose.wanandroid.ui.widget.CenterAppBar

@Composable
fun QuestionPage(viewModel: QuestionViewModel = viewModel()) {
    Scaffold(
        topBar = {
            CenterAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "问答",
                        fontSize = 16.sp,
                        color = AppTheme.colors.onPrimary
                    )
                },
                backgroundColor = AppTheme.colors.primary
            )
        },
        modifier = Modifier.fillMaxSize(),
        backgroundColor = AppTheme.colors.background,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.background)
    ) { innerPadding ->
        val pagingItems = viewModel.pager.collectAsLazyPagingItems()

        RefreshList(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            lazyPagingItems = pagingItems,
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
}