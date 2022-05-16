package com.compose.wanandroid.ui.page.profile.bookmark

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
import com.compose.wanandroid.data.local.model.Bookmark
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.widget.RefreshList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun NavGraphBuilder.bookmarkGraph(controller: NavController) {
    composable(route = Page.Bookmark.route) {
        BookmarkPage(controller)
    }
}

@Composable
fun BookmarkPage(
    controller: NavController,
    viewModel: BookmarkViewModel = viewModel()
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
        title = "我的书签",
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
                    itemsIndexed(pagingItems) { _: Int, bookmark: Bookmark? ->
                        if (bookmark != null) {
                            LinkItem(
                                title = bookmark.title,
                                link = formatter.format(Date(bookmark.time)),
                                modifier = Modifier.clickable {
                                    controller.navigate(Page.Web.route, Link(bookmark.link, bookmark.title))
                                })
                        }
                    }
                }
            )
        }
    }
}