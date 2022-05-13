package com.compose.wanandroid.ui.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.page.home.HomePage
import com.compose.wanandroid.ui.page.profile.ProfilePage
import com.compose.wanandroid.ui.page.question.QuestionPage
import com.compose.wanandroid.ui.page.category.CategoryPage
import com.compose.wanandroid.ui.page.collect.collectGraph
import com.compose.wanandroid.ui.page.detail.webGraph
import com.compose.wanandroid.ui.page.login.loginGraph
import com.compose.wanandroid.ui.page.profile.settingGraph
import com.compose.wanandroid.ui.page.struct.categoryGraph
import com.compose.wanandroid.ui.theme.*

@Preview
@Composable
fun MainPagePreview() {
    AppTheme {
        MainPage()
    }
}

@Composable
fun MainPage() {
    val controller = rememberNavController()
    val stackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = stackEntry?.destination?.route

    AppScaffold(
        topBar = {},
        content = { NavigationHost(controller, it) },
        bottomBar = {
            // 仅在主页展示BottomBar
            when (currentRoute) {
                null, Page.Home.route, Page.Question.route, Page.Category.route, Page.Profile.route -> {
                    BottomNavigation(controller, currentRoute)
                }
            }
        }
    )
}

@Composable
private fun NavigationHost(
    controller: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = controller,
        startDestination = Page.Home.route,
        modifier = Modifier.background(AppTheme.colors.background)
    ) {
        composable(Page.Home.route) { HomePage(controller, padding) }
        composable(Page.Question.route) { QuestionPage(controller, padding) }
        composable(Page.Category.route) { CategoryPage(controller, padding) }
        composable(Page.Profile.route) { ProfilePage(controller, padding) }

        loginGraph { controller.back() }
        settingGraph { controller.back() }
        webGraph { controller.back() }
        collectGraph(controller)
        categoryGraph(controller)
    }
}

@Composable
private fun BottomNavigation(
    controller: NavController,
    currentRoute: String?
) {
    val pages = listOf(
        Page.Home,
        Page.Question,
        Page.Category,
        Page.Profile
    )
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        backgroundColor = AppTheme.colors.tabBackground,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.background)
    ) {
        pages.forEach { tab ->
            BottomNavigationItem(
                selected = tab.route == currentRoute,
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
                label = { Text(text = tab.text, fontSize = 11.sp) },
                onClick = {
                    if (tab.route != currentRoute) {
                        controller.navigate(tab.route) {
                            popUpTo(controller.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = true,
                selectedContentColor = AppTheme.colors.highlight,
                unselectedContentColor = AppTheme.colors.textSecondary,
            )
        }
    }
}