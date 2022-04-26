package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.compose.wanandroid.ui.theme.AppTheme

@Composable
fun HomePage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Text("首页", color = AppTheme.colors.textPrimary)
        Text("首页", color = AppTheme.colors.textPrimary)
        Text("首页", color = AppTheme.colors.textPrimary)
        Text("首页", color = AppTheme.colors.textPrimary)
        Text("首页", color = AppTheme.colors.textPrimary)
        Text("首页", color = AppTheme.colors.textPrimary)
    }
}