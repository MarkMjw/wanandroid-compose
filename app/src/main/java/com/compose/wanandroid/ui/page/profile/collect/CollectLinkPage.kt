package com.compose.wanandroid.ui.page.profile.collect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectLinkPage(
    controller: NavController,
    scaffoldState: ScaffoldState,
    viewModel: CollectLinkViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect { event ->
            when (event) {
                is SnackViewEvent -> {
                    showDialog = false
                    scope.launch {
                        scaffoldState.showSnackbar(message = event.message)
                    }
                }

                is ProgressViewEvent -> showDialog = event.show
            }
        }
    }

    if (showDialog) {
        ProgressDialog("加载中...") {
            showDialog = false
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
                    RevealSwipe(
                        closeOnContentClick = true,
                        directions = setOf(RevealDirection.EndToStart),
                        maxRevealDp = 88.dp,
                        hiddenContentEnd = {
                            ActionMenu(
                                actionIconSize = 56.dp,
                                onDelete = {
                                    viewModel.dispatch(CollectLinkViewAction.UnCollect(link))
                                }
                            )
                        },
                        animateBackgroundCardColor = false,
                        backgroundCardEndColor = AppTheme.colors.primary
                    ) {
                        LinkItem(
                            title = link.name,
                            link = link.link,
                            modifier = Modifier
                                .background(AppTheme.colors.background)
                                .clickable {
                                    controller.navigate(Page.Web.route, Link(title = link.name, url = link.link))
                                }
                        )
                    }
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
fun ActionMenu(
    actionIconSize: Dp,
    onDelete: () -> Unit,
//    onEdit: () -> Unit,
//    onFavorite: () -> Unit,
) {
    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = {
                onDelete()
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bin),
                    tint = AppTheme.colors.onPrimary,
                    contentDescription = "delete action",
                )
            }
        )
//        IconButton(
//            modifier = Modifier.size(actionIconSize),
//            onClick = onEdit,
//            content = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_edit),
//                    tint = AppTheme.colors.onPrimary,
//                    contentDescription = "edit action",
//                )
//            },
//        )
//        IconButton(
//            modifier = Modifier.size(actionIconSize),
//            onClick = onFavorite,
//            content = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_favorite),
//                    tint = AppTheme.colors.onPrimary,
//                    contentDescription = "Expandable Arrow",
//                )
//            }
//        )
    }
}