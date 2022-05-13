package com.compose.wanandroid.ui.page.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.fromJson
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.progress
import com.compose.wanandroid.ui.widget.StatePageEmpty
import com.google.accompanist.web.*

fun NavGraphBuilder.webGraph(onBack: () -> Unit) {
    composable(
        route = Page.Web.route + "/{link}",
        arguments = listOf(navArgument("link") { type = NavType.StringType })
    ) {
        val link = it.arguments?.getString("link")?.fromJson<Link>()
        if (link != null) {
            WebPage(link, onBack)
        }
    }
}

@Composable
fun WebPage(
    link: Link,
    onBack: () -> Unit = {}
) {
    val state = rememberWebViewState(url = link.url)
    val navigator = rememberWebViewNavigator()
    val isDark = !AppTheme.colors.isLight

    AppScaffold(
        title = state.pageTitle ?: link.title,
        onBack = {
            if (navigator.canGoBack) {
                navigator.navigateBack()
            } else {
                onBack()
            }
        }
    ) {
        if (link.url.isNotEmpty()) {
            val loadingState = state.loadingState
            Logger.i("Web", link.url)
            Box(modifier = Modifier.fillMaxSize()) {
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