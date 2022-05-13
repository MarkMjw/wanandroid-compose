package com.compose.wanandroid.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.defaultContentColorFor
import com.compose.wanandroid.ui.widget.CenterAppBar

suspend fun ScaffoldState.showSnackbar(message: String, actionLabel: String? = null, duration: SnackbarDuration = SnackbarDuration.Short) {
    snackbarHostState.showSnackbar(message, actionLabel, duration)
}

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    onBack: () -> Unit = {},
    topBar: @Composable () -> Unit = { AppTitleBar(text = title, onBack = onBack) },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        backgroundColor = AppTheme.colors.background,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.background),
        snackbarHost = { AppSnackbar(scaffoldState) },
        scaffoldState = scaffoldState,
        bottomBar = bottomBar,
        content = content
    )
}

@Composable
fun AppTitleBar(
    modifier: Modifier = Modifier,
    text: String = "",
    onBack: () -> Unit = {},
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    leadingActions: @Composable (RowScope.() -> Unit) = {
        IconButton(
            onClick = { onBack() },
            content = {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    tint = AppTheme.colors.onPrimary,
                    contentDescription = "Back"
                )
            })
    },
    trailingActions: @Composable (RowScope.() -> Unit) = { },
    title: @Composable () -> Unit = {
        Text(
            text = text,
            fontSize = 16.sp,
            color = AppTheme.colors.onPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
) {
    CenterAppBar(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        backgroundColor = AppTheme.colors.primary,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.primary),
        leadingActions = leadingActions,
        title = title,
        trailingActions = trailingActions
    )
}

@Composable
fun AppSnackbar(scaffoldState: ScaffoldState) {
    SnackbarHost(hostState = scaffoldState.snackbarHostState) { msg ->
        Snackbar(
            snackbarData = msg,
            backgroundColor = AppTheme.colors.secondaryBackground,
            actionColor = AppTheme.colors.onBackground,
            contentColor = AppTheme.colors.textPrimary,
            shape = AppTheme.shapes.medium
        )
    }
}