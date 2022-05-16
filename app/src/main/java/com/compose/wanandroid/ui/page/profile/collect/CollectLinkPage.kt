package com.compose.wanandroid.ui.page.profile.collect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.LinkItem
import com.compose.wanandroid.ui.common.RefreshViewAction
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.showSnackbar
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.StatePage
import kotlinx.coroutines.launch

@Composable
fun CollectLinkPage(
    controller: NavController,
    scaffoldState: ScaffoldState,
    viewModel: CollectLinkViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is SnackViewEvent -> {
                    scope.launch {
                        scaffoldState.showSnackbar(message = it.message)
                    }
                }
            }
        }
    }

    StatePage(
        modifier = Modifier.fillMaxSize(),
        state = viewState.pageState,
        onRetry = {
            viewModel.dispatch(RefreshViewAction.FetchData)
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
                        title = link.name,
                        link = link.link,
                        modifier = Modifier.clickable {
                            controller.navigate(Page.Web.route, Link(title = link.name, url = link.link))
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