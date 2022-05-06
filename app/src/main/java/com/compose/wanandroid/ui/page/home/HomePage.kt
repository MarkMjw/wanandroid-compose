package com.compose.wanandroid.ui.page.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Banner
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.ArticleItem
import com.compose.wanandroid.ui.common.RefreshList
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.defaultContentColorFor
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.AppBarHeight
import com.compose.wanandroid.ui.widget.Banner
import com.compose.wanandroid.ui.widget.CenterAppBar
import com.compose.wanandroid.ui.widget.rememberBannerState

@Preview(widthDp = 420, heightDp = 720)
@Composable
fun HomePagePreview() {
    AppTheme {
        HomePage()
    }
}

@Composable
fun HomePage(viewModel: HomeViewModel = viewModel()) {
    val viewState = viewModel.viewState
    val pagingItems = viewState.pagingData.collectAsLazyPagingItems()
    val banners = viewState.banners
    val tops = viewState.tops
    val isRefreshing = viewState.isRefreshing
    val listState = if (pagingItems.itemCount > 0) viewState.listState else LazyListState()

    Scaffold(
        topBar = {
            CenterAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = AppTheme.colors.primary,
                contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.primary),
                leftActions = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_scan),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(AppBarHeight),
                        colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )
                },
                title = {
                    Text(
                        text = "首页",
                        fontSize = 16.sp,
                        color = AppTheme.colors.onPrimary
                    )
                },
                rightActions = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(AppBarHeight),
                        colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        backgroundColor = AppTheme.colors.background,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.background)
    ) { innerPadding ->
        val context = LocalContext.current
        RefreshList(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = isRefreshing,
            lazyPagingItems = pagingItems,
            listState = listState,
            onRefresh = {
                viewModel.dispatch(HomeViewAction.Refresh)
            },
            itemContent = {
                if (banners.isNotEmpty()) {
                    item {
                        Banner(banners)
                    }
                }

                if (tops.isNotEmpty()) {
                    itemsIndexed(tops) { index: Int, value: Article? ->
                        if (value != null) {
                            ArticleItem(
                                data = value,
                                isTop = true,
                                modifier = Modifier.padding(top = if (index == 0) 5.dp else 0.dp),
                                onCollectClick = { id ->
                                    "收藏:$id".toast(context)
                                },
                                onUserClick = { id ->
                                    "用户:$id".toast(context)
                                },
                                onSelected = {
                                    "$it".toast(context)
                                }
                            )
                        }
                    }
                }

                itemsIndexed(pagingItems) { _: Int, value: Article? ->
                    if (value != null) {
                        ArticleItem(data = value)
                    }
                }
            })
    }
}

@Composable
private fun Banner(banners: List<Banner>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.78f)
    ) {
        val bannerState = rememberBannerState()

        Banner(
            count = banners.size,
            state = bannerState
        ) { page ->
            AsyncImage(
                model = banners[page].imagePath,
                placeholder = painterResource(id = R.drawable.image_holder),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppTheme.colors.secondaryBackground)
                    .clickable {
                        with(banners[page]) {
                            Logger.w("mjw", "$title -> $url")
                        }
                    },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp, start = 6.dp, end = 6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in banners.indices) {
                    var size by remember { mutableStateOf(5.dp) }
                    size = if (bannerState.currentPage == i) 7.dp else 5.dp
                    val color = if (bannerState.currentPage == i) AppTheme.colors.secondary else AppTheme.colors.textThird
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color)
                            // 当size改变的时候以动画的形式改变
                            .animateContentSize()
                            .size(size)
                    )
                    // 指示点间的间隔
                    if (i != banners.lastIndex) {
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
        }
    }
}