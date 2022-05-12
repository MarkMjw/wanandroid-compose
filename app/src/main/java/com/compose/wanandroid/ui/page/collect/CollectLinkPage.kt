package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.data.model.CollectLink
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.StatePage
import com.compose.wanandroid.ui.widget.html.HtmlText

@Composable
fun CollectLinkPage(
    navController: NavController,
    viewModel: CollectLinkViewModel = viewModel()
) {
    val viewState = viewModel.viewState
    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewState.pageState,
        onRetry = {
            viewModel.dispatch(CollectLinkViewAction.FetchData)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background),
            state = viewState.listState
        ) {
            viewState.data.forEachIndexed { position, link ->
                item {
                    LinkItem(
                        link = link,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Web.route, Link(title = link.name, url = link.link))
                        })

                    if (position <= viewState.size - 1) {
                        Divider(
                            startIndent = 10.dp,
                            color = AppTheme.colors.secondaryBackground,
                            thickness = 0.8f.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LinkItem(
    link: CollectLink,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HtmlText(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = link.name,
            color = AppTheme.colors.textPrimary,
            fontSize = 15.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            urlSpanStyle = SpanStyle(textDecoration = null)
        )

        Text(
            text = link.link,
            fontSize = 12.sp,
            color = AppTheme.colors.textThird,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}