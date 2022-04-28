package com.compose.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.compose.wanandroid.ui.page.main.MainPage
import com.compose.wanandroid.ui.theme.AppSurface
import com.compose.wanandroid.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, true)
//        transparentStatusBar()
        setContent {
            AppTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(color = AppTheme.colors.primary)
                systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
                AppSurface(modifier = Modifier.fillMaxSize()) {
                    MainPage()
                }
            }
        }
    }
}