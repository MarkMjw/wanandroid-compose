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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.fromJson
import com.compose.wanandroid.ui.page.home.HomePage
import com.compose.wanandroid.ui.page.profile.ProfilePage
import com.compose.wanandroid.ui.page.question.QuestionPage
import com.compose.wanandroid.ui.page.category.CategoryPage
import com.compose.wanandroid.ui.page.detail.WebPage
import com.compose.wanandroid.ui.page.profile.SettingPage
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

    Scaffold(
        bottomBar = {
            // 仅在主页展示BottomBar
            when (currentRoute) {
                null, Screen.Home.route, Screen.Question.route, Screen.Category.route, Screen.Profile.route -> {
                    BottomNavigation(controller, currentRoute)
                }
            }
        },
        content = { padding ->
            NavigationHost(controller, padding)
        }
    )
}

@Composable
private fun NavigationHost(controller: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = controller,
        startDestination = Screen.Home.route,
        modifier = Modifier
            .background(AppTheme.colors.background)
    ) {
        composable(Screen.Home.route) { HomePage(controller, padding) }
        composable(Screen.Question.route) { QuestionPage(controller, padding) }
        composable(Screen.Category.route) { CategoryPage(controller, padding) }
        composable(Screen.Profile.route) { ProfilePage(controller, padding) }

        composable(
            route = Screen.Web.route + "/{link}",
            arguments = listOf(navArgument("link") { type = NavType.StringType })
        ) {
            val args = it.arguments?.getString("link")?.fromJson<Link>()
            if (args != null) {
                WebPage(args, controller)
            }
        }

        composable(route = Screen.Setting.route) {
            SettingPage(controller)
        }
    }
}

@Composable
private fun BottomNavigation(
    controller: NavController,
    currentRoute: String?
) {
    val screens = listOf(
        Screen.Home,
        Screen.Question,
        Screen.Category,
        Screen.Profile
    )
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        backgroundColor = AppTheme.colors.tabBackground,
        contentColor = defaultContentColorFor(backgroundColor = AppTheme.colors.background)
    ) {
        screens.forEach { tab ->
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