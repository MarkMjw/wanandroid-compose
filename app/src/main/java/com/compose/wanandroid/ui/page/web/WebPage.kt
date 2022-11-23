package com.compose.wanandroid.ui.page.web

import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.data.repository.HistoryRepository
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.fromJson
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.progress
import com.compose.wanandroid.ui.widget.StatePageEmpty
import com.google.accompanist.web.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val historyRepo: HistoryRepository = get()

    AppScaffold(
        topBar = {
            AppTitleBar(
                text = state.pageTitle ?: link.title,
                onBack = {
                    if (navigator.canGoBack) {
                        navigator.navigateBack()
                    } else {
                        onBack()
                    }
                },
                trailingActions = {
                    IconButton(
                        onClick = {
                            CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(link.url))
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.OpenInBrowser,
                                tint = AppTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        }
    ) {
        if (link.url.isNotEmpty()) {
            val loadingState = state.loadingState
            Logger.i("Web", link.url)
            Box(modifier = Modifier.fillMaxSize()) {
                WebView(
                    state = state,
                    onCreated = { webView ->
                        webView.isVerticalScrollBarEnabled = false
                        webView.settings.run {
                            javaScriptEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                            domStorageEnabled = true
                            databaseEnabled = true
                            allowFileAccess = true
                            javaScriptCanOpenWindowsAutomatically = true
                            loadsImagesAutomatically = true
                            defaultTextEncodingName = "UTF-8"
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // fix图片无法显示(https与http混合资源处理)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                                WebSettingsCompat.setAlgorithmicDarkeningAllowed(this, isDark)
                            } else if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                                if (isDark) {
                                    WebSettingsCompat.setForceDark(this, WebSettingsCompat.FORCE_DARK_ON)
                                } else {
                                    WebSettingsCompat.setForceDark(this, WebSettingsCompat.FORCE_DARK_OFF)
                                }
                            }
                        }
                    },
                    navigator = navigator,
                    client = object : AccompanistWebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val url = request?.url?.scheme ?: ""
                            return if (url.startsWith("http", ignoreCase = true)) {
                                super.shouldOverrideUrlLoading(view, request)
                            } else {
                                true
                            }
                        }
                    },
                    chromeClient = object : AccompanistWebChromeClient() {
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            if (link.addHistory) {
                                scope.launch {
                                    if (view?.url == link.url) {
                                        historyRepo.add(link.url, title ?: link.title)
                                    }
                                }
                            }
                        }
                    }
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