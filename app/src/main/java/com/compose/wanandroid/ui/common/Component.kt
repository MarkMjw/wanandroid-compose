package com.compose.wanandroid.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.R
import com.compose.wanandroid.ui.theme.AppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun <T : Any> RefreshList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit) = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit,
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    SwipeRefresh(
        modifier = modifier,
        state = refreshState,
        onRefresh = {
            onRefresh()
            lazyPagingItems.refresh()
        }) {
        // 刷新状态
        refreshState.isRefreshing = (lazyPagingItems.loadState.refresh is LoadState.Loading) || isRefreshing

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            // 列表Item
            itemContent()

            // 结束Item
            if (!refreshState.isRefreshing) {
                item {
                    lazyPagingItems.loadState.run {
                        when (append) {
                            is LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem()
                            is LoadState.NotLoading -> {
                                if (append.endOfPaginationReached) {
                                    EndItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.primary,
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "加载中...",
            fontSize = 13.sp,
            modifier = Modifier
                .wrapContentSize(),
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textSecondary
        )
    }
}

@Composable
fun ErrorItem() {
    Text(
        text = "加载失败，请重试",
        fontSize = 13.sp,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = AppTheme.colors.textSecondary
    )
}

@Composable
fun EndItem() {
    Image(
        painter = painterResource(id = R.drawable.icon_load_end),
        contentDescription = null,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Inside,
        alignment = Alignment.Center
    )
}
