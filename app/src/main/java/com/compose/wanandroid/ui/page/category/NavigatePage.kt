@file:OptIn(ExperimentalFoundationApi::class)

package com.compose.wanandroid.ui.page.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.data.model.Navigate
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.StickyTitle
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.StatePage
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NavigatePage(
    navController: NavController,
    viewModel: NavigateViewModel = viewModel()
) {
    val viewState = viewModel.viewState
    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewState.pageState,
        onRetry = {
            viewModel.dispatch(NavigateViewAction.FetchData)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background),
            state = viewState.listState,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            viewState.data.forEachIndexed { position, navigate ->
                stickyHeader { StickyTitle(navigate.name) }
                item {
                    NavigateItem(navigate, onSelect = {
                        navController.navigate(Page.Web.route, it)
                    })
                    if (position <= viewState.size - 1) {
                        Divider(
                            startIndent = 10.dp,
                            color = AppTheme.colors.secondaryBackground,
                            thickness = 0.8f.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun NavigateItem(
    navigate: Navigate,
    modifier: Modifier = Modifier,
    onSelect: (Link) -> Unit = { },
) {
    if (navigate.articles.isNotEmpty()) {
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 16.dp)
        ) {
            navigate.articles.forEach { item ->
                Text(
                    text = item.title,
                    modifier = modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .height(25.dp)
                        .background(color = AppTheme.colors.secondaryBackground, shape = RoundedCornerShape(25.dp / 2))
                        .clip(shape = RoundedCornerShape(25.dp / 2))
                        .clickable {
                            onSelect(Link(item.link, item.title))
                        }
                        .padding(
                            horizontal = 10.dp,
                            vertical = 3.dp
                        ),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.textSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}