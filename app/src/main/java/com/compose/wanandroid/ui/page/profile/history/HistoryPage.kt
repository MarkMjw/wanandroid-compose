package com.compose.wanandroid.ui.page.profile.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.compose.wanandroid.ui.widget.StatePage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.data.local.model.History
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.widget.RefreshList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun NavGraphBuilder.historyGraph(controller: NavController) {
    composable(route = Page.History.route) {
        HistoryPage(controller)
    }
}

@Composable
fun HistoryPage(
    controller: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val pagingItems = viewModel.pager.collectAsLazyPagingItems()
    val scaffoldState = rememberScaffoldState()
    val formatter by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA))
    }

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

    AppScaffold(
        scaffoldState = scaffoldState,
        title = "阅读历史",
        onBack = { controller.back() },
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
                    itemsIndexed(pagingItems) { _: Int, history: History? ->
                        if (history != null) {
                            LinkItem(
                                title = history.title,
                                link = formatter.format(Date(history.lastTime)),
                                modifier = Modifier.clickable {
                                    controller.navigate(Page.Web.route, Link(history.link, history.title))
                                })
                        }
                    }
                })
        }
    }
}