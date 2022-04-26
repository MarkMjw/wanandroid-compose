package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.data.model.authorName
import com.compose.wanandroid.ui.common.RefreshList
import com.compose.wanandroid.ui.theme.AppTheme

@Composable
fun HomePage(viewModel: HomeViewModel = HomeViewModel()) {
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

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    data: Article,
    isTop: Boolean = false,
    onSelected: (Link) -> Unit = {},
    onCollectClick: (articleId: Int) -> Unit = {},
    onUserClick: (userId: Int) -> Unit = {},
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .background(color = AppTheme.colors.background)
            .fillMaxWidth()
    ) {
        Text(
            text = data.authorName,
            color = AppTheme.colors.textPrimary,
            fontSize = 15.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Start)
        )

        Text(
            text = data.title,
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Start)
                .padding(top = 10.dp, bottom = 20.dp)
        )
    }
}