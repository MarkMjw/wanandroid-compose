package com.compose.wanandroid.ui.page.main

import androidx.annotation.DrawableRes
import com.compose.wanandroid.R

enum class MainTab(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
) {
    TAB_HOME("首页", R.drawable.ic_bottom_bar_home, "home"),
    TAB_QUESTION("问答", R.drawable.ic_bottom_bar_ques, "question"),
    TAB_SYSTEM("体系", R.drawable.ic_bottom_bar_navi, "system"),
    TAB_PROFILE("我的", R.drawable.ic_bottom_bar_mine, "profile")
}