package com.compose.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.compose.wanandroid.ui.page.main.MainPage
import com.compose.wanandroid.ui.theme.AppSurface
import com.compose.wanandroid.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        transparentStatusBar()
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                AppSurface(modifier = Modifier.fillMaxSize()) {
                    MainPage()
                }
            }
        }
    }
}