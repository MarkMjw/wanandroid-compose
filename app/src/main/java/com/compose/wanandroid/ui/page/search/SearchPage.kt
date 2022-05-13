package com.compose.wanandroid.ui.page.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.logic.noRippleClickable
import com.compose.wanandroid.logic.toast
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.*
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

fun NavGraphBuilder.searchGraph(controller: NavController) {
    composable(Page.Search.route) {
        SearchPage(controller)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchPage(
    controller: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val viewState = viewModel.viewState
    val pagingItems = viewState.pagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is SnackViewEvent -> {
                    scope.launch {
                        scaffoldState.showSnackbar(it.message)
                    }
                }
                is SearchViewEvent.Searching -> pagingItems.refresh()
            }
        }
    }

    var keyword by remember { mutableStateOf("") }

    AppScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppTitleBar(
                onBack = {
                    keyboardController?.hide()
                    controller.back()
                },
                title = {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        text = keyword,
                        onValueChanged = {
                            keyword = it
                        },
                        hint = "支持多个关键词，用空格隔开"
                    )
                },
                trailingActions = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            viewModel.dispatch(SearchViewAction.Search(keyword))
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                tint = AppTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    )
                })
        }
    ) {
        if (pagingItems.itemCount > 0) {
            RefreshList(
                lazyPagingItems = pagingItems,
                modifier = Modifier.fillMaxSize(),
                listState = viewState.listState,
                itemContent = {
                    itemsIndexed(pagingItems) { _: Int, value: Article? ->
                        if (value != null) {
                            ArticleItem(data = value,
                                onCollectClick = {
                                    viewModel.dispatch(CollectViewAction.Collect(it))
                                },
                                onUserClick = { id ->
                                    "用户:$id".toast(context)
                                },
                                onSelected = {
                                    controller.navigate(Page.Web.route, it)
                                }
                            )
                        }
                    }
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = viewState.hotKeys.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    HotKeyItem(title = "热门搜索", list = viewState.hotKeys) {
                        keyword = it
                        keyboardController?.hide()
                        viewModel.dispatch(SearchViewAction.Search(keyword))
                    }
                }

                AnimatedVisibility(
                    visible = viewState.historyKeys.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    HotKeyItem(
                        title = "历史搜索",
                        subTitle = "清除",
                        onSubTitleClick = {
                            viewModel.dispatch(SearchViewAction.ClearHistory)
                        },
                        list = viewState.historyKeys
                    ) {
                        keyword = it
                        keyboardController?.hide()
                        viewModel.dispatch(SearchViewAction.Search(keyword))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = ""
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .background(Color.Black.copy(0.08f), RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChanged,
            singleLine = true,
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp)
                .wrapContentHeight()
                .weight(1f),
            textStyle = TextStyle(
                color = AppTheme.colors.onPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            cursorBrush = SolidColor(AppTheme.colors.onPrimary),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(all = 0.dp),
                            text = hint,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = AppTheme.colors.onPrimary.copy(0.6f),
                        )
                    }
                    innerTextField()
                }
            }
        )

        AnimatedVisibility(
            visible = text.trim().isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
                    .noRippleClickable {
                        onValueChanged("")
                    }
            )
        }
    }
}

@Composable
fun HotKeyItem(
    title: String,
    list: List<String>,
    modifier: Modifier = Modifier,
    subTitle: String = "",
    onSubTitleClick: () -> Unit = {},
    onSelect: (key: String) -> Unit = { }
) {
    if (list.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    fontSize = 15.sp,
                    color = AppTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                )

                if (subTitle.isNotEmpty()) {
                    Text(
                        text = subTitle,
                        fontSize = 12.sp,
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 10.dp)
                            .clickable {
                                onSubTitleClick()
                            }
                    )
                }
            }

            FlowRow(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 16.dp)
            ) {
                list.forEach { item ->
                    Box(modifier = modifier
                        .padding(start = 10.dp, bottom = 10.dp)
                        .height(28.dp)
                        .background(color = AppTheme.colors.secondaryBackground, shape = RoundedCornerShape(14.dp))
                        .clip(shape = RoundedCornerShape(14.dp))
                        .clickable {
                            onSelect(item)
                        }
                        .padding(
                            horizontal = 10.dp,
                            vertical = 3.dp
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
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
    }
}