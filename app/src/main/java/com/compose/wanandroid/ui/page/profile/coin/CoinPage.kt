package com.compose.wanandroid.ui.page.profile.coin

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.wanandroid.ui.widget.StatePage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.model.Coin
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.RefreshList

fun NavGraphBuilder.coinGraph(controller: NavController) {
    composable(
        route = Page.Coin.route + "/{count}",
        arguments = listOf(navArgument("count") { type = NavType.IntType })
    ) {
        val count = it.arguments?.getInt("count", 0) ?: 0
        CoinPage(controller, count)
    }
}

@Composable
fun CoinPage(
    controller: NavController,
    coinCount: Int,
    viewModel: CoinViewModel = viewModel()
) {
    val pagingItems = viewModel.pager.collectAsLazyPagingItems()

    AppScaffold(
        topBar = {
            AppTitleBar(
                text = "我的积分（$coinCount）",
                onBack = { controller.back() },
                trailingActions = {
                    IconButton(
                        onClick = {
                            controller.navigate(
                                Page.Web.route,
                                Link(title = "本站积分规则", url = "https://www.wanandroid.com/blog/show/2653", addHistory = false)
                            )
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.HelpOutline,
                                tint = AppTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        }
    ) {
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
                    itemsIndexed(pagingItems) { _: Int, coin: Coin? ->
                        if (coin != null) {
                            CoinItem(coin)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CoinItem(coin: Coin) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = coin.title,
                color = AppTheme.colors.textPrimary,
                fontSize = 15.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = coin.time,
                fontSize = 12.sp,
                color = AppTheme.colors.textThird,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(top = 5.dp)
            )
        }

        Text(
            text = coin.count,
            fontSize = 15.sp,
            color = AppTheme.colors.highlight,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}