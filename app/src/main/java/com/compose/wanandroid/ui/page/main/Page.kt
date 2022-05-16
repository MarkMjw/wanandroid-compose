package com.compose.wanandroid.ui.page.main

import androidx.annotation.DrawableRes
import com.compose.wanandroid.R

sealed class Page(
    val route: String,
    val text: String = "",
    @DrawableRes val icon: Int = 0
) {
    object Home : Page("home", "首页", R.drawable.ic_bottom_bar_home)
    object Square : Page("square", "广场", R.drawable.ic_bottom_bar_ques)
    object Struct : Page("struct", "体系", R.drawable.ic_bottom_bar_navi)
    object Profile : Page("profile", "我的", R.drawable.ic_bottom_bar_mine)
    object Web : Page("web")
    object Login : Page("login")
    object Search : Page("search", "搜索")
    object Collect : Page("collect")
    object Setting : Page("setting", "设置")
    object Category : Page("category")
    object MineShare : Page("mine_share")
    object Bookmark : Page("bookmark")
    object History : Page("history")
    object Coin : Page("coin")
    object Opensource : Page("opensource")
}