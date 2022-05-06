package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class PageState {
    object Loading : PageState()
    data class Success(val isEmpty: Boolean) : PageState()
    data class Error(val exception: Throwable) : PageState()
}

@Composable
fun StatePage(
    state: PageState,
    onRetry: () -> Unit = {},
    content: @Composable () -> Unit
) = when (state) {
    is PageState.Loading -> StatePageLoading()
    is PageState.Error -> StatePageError(onClick = onRetry)
    is PageState.Success -> {
        if (state.isEmpty) {
            StatePageEmpty()
        } else {
            content()
        }
    }
}

@Composable
fun StatePageLoading(
    modifier: Modifier = Modifier,
    text: String = "加载中",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
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
    text: String = "出错了",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    secondaryText: String? = "请稍后重试",
    secondaryTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
    textColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
    secondaryTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled),
    image: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = textColor
        )
    },
    onClick: () -> Unit = {},
) {
    val clickState = rememberUpdatedState(onClick)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
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
        secondaryText?.let {
            Text(
                text = secondaryText,
                style = secondaryTextStyle,
                color = secondaryTextColor,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { clickState.value() },
            modifier = Modifier
                .width(80.dp)
                .padding(top = 8.dp)
        ) {
            Text(text = "重试")
        }
    }
}

@Composable
fun StatePageEmpty(
    modifier: Modifier = Modifier,
    text: String = "暂无数据",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled),
    image: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Outlined.Refresh,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = textColor
        )
    }
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
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