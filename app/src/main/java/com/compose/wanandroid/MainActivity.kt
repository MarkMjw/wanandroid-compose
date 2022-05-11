package com.compose.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.compose.wanandroid.logic.darkMode
import com.compose.wanandroid.ui.page.main.MainPage
import com.compose.wanandroid.ui.theme.AppSurface
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.ThemeState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            splash.setKeepOnScreenCondition { true }
            delay(500)
            splash.setKeepOnScreenCondition { false }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            // 读取主题配置
            ThemeState.read(darkMode.collectAsState("").value)
            AppTheme {
                val systemUiController = rememberSystemUiController()
//                systemUiController.setSystemBarsColor(color = AppTheme.colors.primary)
//                systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = false
                    )
                }
                AppSurface(modifier = Modifier.fillMaxSize()) {
                    MainPage()
                }
            }
        }
    }
}