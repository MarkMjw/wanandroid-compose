package com.compose.wanandroid.ui.page.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.progress
import com.compose.wanandroid.ui.widget.CenterAppBar
import com.compose.wanandroid.ui.widget.StatePageEmpty
import com.google.accompanist.web.*

@Composable
fun WebPage(
    link: Link,
    navController: NavController = rememberNavController()
) {
    val state = rememberWebViewState(url = link.url)
    val navigator = rememberWebViewNavigator()
    val isDark = !AppTheme.colors.isLight

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAppBar(
            modifier = Modifier.fillMaxWidth(),
            leadingActions = {
                IconButton(onClick = {
                    if (navigator.canGoBack) {
                        navigator.navigateBack()
                    } else {
                        navController.back()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = AppTheme.colors.onPrimary,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Text(
                    text = state.pageTitle ?: link.title,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.onPrimary
                )
            },
            backgroundColor = AppTheme.colors.primary
        )

        if (link.url.isNotEmpty()) {
            val loadingState = state.loadingState
            Logger.i("Web", link.url)
            Box(modifier = Modifier.weight(1f)) {
                WebView(
                    state = state,
                    onCreated = { webView ->
                        webView.settings.run {
                            javaScriptEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true

                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false

                            allowFileAccess = true
                            javaScriptCanOpenWindowsAutomatically = true
                            loadsImagesAutomatically = true
                            defaultTextEncodingName = "UTF-8"

                            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                                if (isDark) {
                                    WebSettingsCompat.setForceDark(this, WebSettingsCompat.FORCE_DARK_ON)
                                } else {
                                    WebSettingsCompat.setForceDark(this, WebSettingsCompat.FORCE_DARK_OFF)
                                }
                            }
                        }
                    },
                    navigator = navigator,
                )

                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        color = AppTheme.colors.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    )
                }
            }
        } else {
            StatePageEmpty(
                modifier = Modifier.fillMaxSize(),
                textColor = AppTheme.colors.textSecondary
            )
        }
    }
}