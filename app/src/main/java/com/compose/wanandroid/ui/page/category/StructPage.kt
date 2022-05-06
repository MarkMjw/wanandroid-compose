@file:OptIn(ExperimentalFoundationApi::class)

package com.compose.wanandroid.ui.page.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.wanandroid.ui.widget.StatePage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.ui.common.StickyTitle
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.AppThemePreview
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun StructPage(viewModel: StructViewModel = viewModel()) {
    val viewState = viewModel.viewState
    StatePage(
        state = viewState.pageState,
        onRetry = {
            viewModel.dispatch(StructViewAction.FetchData)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(AppTheme.colors.background),
            state = viewState.listState,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            viewState.data.forEachIndexed { position, struct ->
                stickyHeader { StickyTitle(title = struct.name) }
                item {
                    StructItem(struct, onSelect = { parent ->

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
fun StructItem(
    struct: Struct,
    modifier: Modifier = Modifier,
    onSelect: (parent: Struct) -> Unit = {},
) {
    if (struct.children.isNotEmpty()) {
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 16.dp)
        ) {
            struct.children.forEach { item ->
                Text(
                    text = item.name,
                    modifier = modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .height(25.dp)
                        .combinedClickable(
                            onClick = {
                                onSelect(item)
                            }
                        )
                        .clip(shape = RoundedCornerShape(25.dp / 2))
                        .background(color = AppTheme.colors.secondaryBackground)
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

@Preview
@Composable
fun StructItemPreview() {
    AppThemePreview {
        Column {
            StickyTitle(title = "分类标题")
            StructItem(struct = Struct(name = "分类标题").apply {
                children = mutableListOf(
                    Struct(name = "分类1"),
                    Struct(name = "分类2"),
                    Struct(name = "分类3"),
                    Struct(name = "分类4"),
                    Struct(name = "分类5"),
                    Struct(name = "分类6"),
                    Struct(name = "分类7"),
                    Struct(name = "分类8"),
                    Struct(name = "分类9"),
                    Struct(name = "分类10")
                )
            })
        }
    }
}
