package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
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
import com.compose.wanandroid.ui.theme.progress
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private val ItemHeight = 48.dp

@Composable
fun <T : Any> RefreshList(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit) = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit,
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    SwipeRefresh(
        modifier = modifier,
        state = refreshState,
        indicator = { s, trigger ->
            SwipeRefreshIndicator(
                s, trigger,
                backgroundColor = AppTheme.colors.secondaryBackground,
                contentColor = AppTheme.colors.progress
            )
        },
        onRefresh = {
            onRefresh()
            lazyPagingItems.refresh()
        }) {
        refreshState.isRefreshing = (lazyPagingItems.loadState.refresh is LoadState.Loading) || isRefreshing

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            itemContent()

            if (!refreshState.isRefreshing) {
                item {
                    lazyPagingItems.loadState.run {
                        when (append) {
                            is LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem {
                                lazyPagingItems.retry()
                            }
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
            .height(ItemHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.progress,
            strokeWidth = 3.dp,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "加载中...",
            fontSize = 13.sp,
            modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textSecondary
        )
    }
}

@Composable
fun ErrorItem(retry: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ItemHeight)
            .clickable(onClick = retry),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = AppTheme.colors.textSecondary
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "加载失败，请重试",
            fontSize = 13.sp,
            modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textSecondary
        )
    }
}

@Composable
fun EndItem() {
    Image(
        painter = painterResource(id = R.drawable.icon_load_end),
        contentDescription = null,
        modifier = Modifier
            .height(ItemHeight)
            .fillMaxWidth(),
        contentScale = ContentScale.Inside,
        alignment = Alignment.Center
    )
}
