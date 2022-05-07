package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.AppThemePreview
import com.compose.wanandroid.ui.theme.defaultContentColorFor
import com.compose.wanandroid.ui.theme.progress

sealed class PageState {
    object Loading : PageState()
    data class Success(val isEmpty: Boolean) : PageState()
    data class Error(val exception: Throwable) : PageState()
}

@Preview
@Composable
fun StatePagePreview() {
    AppThemePreview {
        Column(modifier = Modifier.fillMaxSize()) {
            StatePage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                state = PageState.Loading
            ) {}

            StatePage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                state = PageState.Error(Throwable())
            ) {}

            StatePage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                state = PageState.Success(true)
            ) {}
        }
    }
}

@Composable
fun StatePage(
    state: PageState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    content: @Composable () -> Unit
) = when (state) {
    is PageState.Loading -> StatePageLoading(modifier)
    is PageState.Error -> StatePageError(modifier, onClick = onRetry)
    is PageState.Success -> {
        if (state.isEmpty) {
            StatePageEmpty(modifier)
        } else {
            content()
        }
    }
}

@Composable
fun StatePageLoading(
    modifier: Modifier = Modifier,
    text: String = "加载中...",
    textStyle: TextStyle = AppTheme.typography.body1,
    textColor: Color = AppTheme.colors.textPrimary.copy(ContentAlpha.disabled),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(36.dp),
            strokeWidth = 3.dp,
            color = AppTheme.colors.progress
        )
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun StatePageError(
    modifier: Modifier = Modifier,
    text: String = "加载失败",
    textStyle: TextStyle = AppTheme.typography.body1,
    secondaryText: String? = "请检查网络连接后重试",
    secondaryTextStyle: TextStyle = AppTheme.typography.body1.copy(fontSize = 13.sp),
    textColor: Color = AppTheme.colors.textPrimary.copy(ContentAlpha.medium),
    secondaryTextColor: Color = AppTheme.colors.textPrimary.copy(ContentAlpha.disabled),
    image: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Rounded.WifiOff,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = textColor
        )
    },
    onClick: () -> Unit = {},
) {
    val clickState = rememberUpdatedState(onClick)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        image()
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(top = 10.dp)
        )
        secondaryText?.let {
            Text(
                text = secondaryText,
                style = secondaryTextStyle,
                color = secondaryTextColor,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        Button(
            onClick = { clickState.value() },
            shape = RoundedCornerShape(5.dp),
            colors = buttonColors(
                backgroundColor = AppTheme.colors.secondaryBackground,
                contentColor = defaultContentColorFor(AppTheme.colors.secondaryBackground),
                disabledBackgroundColor = AppTheme.colors.secondaryBackground.copy(alpha = 0.12f)
                    .compositeOver(AppTheme.colors.secondaryBackground),
                disabledContentColor = AppTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled)
            ),
            modifier = Modifier
                .padding(top = 10.dp)
                .width(80.dp)
        ) {
            Text(text = "重试")
        }
    }
}

@Composable
fun StatePageEmpty(
    modifier: Modifier = Modifier,
    text: String = "空空如也~",
    textStyle: TextStyle = AppTheme.typography.body1,
    textColor: Color = AppTheme.colors.textPrimary.copy(ContentAlpha.disabled),
    image: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Rounded.FolderOpen,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = textColor
        )
    }
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        image()
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}