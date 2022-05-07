package com.compose.wanandroid.ui.page.main

import androidx.annotation.DrawableRes
import com.compose.wanandroid.R

sealed class Screen(
    val route: String,
    val text: String = "",
    @DrawableRes val icon: Int = 0
) {
    object Home : Screen("home", "首页", R.drawable.ic_bottom_bar_home)
    object Question : Screen("question", "问答", R.drawable.ic_bottom_bar_ques)
    object Category : Screen("category", "分类", R.drawable.ic_bottom_bar_navi)
    object Profile : Screen("profile", "我的", R.drawable.ic_bottom_bar_mine)
    object Web : Screen("web")
    object Login : Screen("login", "登录")
    object Search : Screen("search", "搜索")
    object Collect : Screen("collect", "收藏")
    object Setting : Screen("setting", "设置")
}