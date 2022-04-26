package com.compose.wanandroid.ui.page.main

import androidx.annotation.DrawableRes
import com.compose.wanandroid.R

sealed class Screen(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
) {
    object Home : Screen("首页", R.drawable.ic_bottom_bar_home, "home")
    object Question : Screen("问答", R.drawable.ic_bottom_bar_ques, "question")
    object System : Screen("体系", R.drawable.ic_bottom_bar_navi, "system")
    object Profile : Screen("我的", R.drawable.ic_bottom_bar_mine, "profile")
}