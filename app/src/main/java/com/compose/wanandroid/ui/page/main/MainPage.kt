package com.compose.wanandroid.ui.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.ui.page.home.HomePage
import com.compose.wanandroid.ui.page.profile.ProfilePage
import com.compose.wanandroid.ui.page.question.QuestionPage
import com.compose.wanandroid.ui.page.category.CategoryPage
import com.compose.wanandroid.ui.theme.*

@Preview()
@Composable
fun MainPagePreview() {
    AppThemePreview {
        MainPage()
    }
}

@Composable
fun MainPage() {
    val controller = rememberNavController()
    Scaffold(bottomBar = { BottomNavigation(controller) }) { innerPadding ->
        NavigationHost(controller, innerPadding)
    }
}

@Composable
private fun NavigationHost(controller: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = controller,
        startDestination = Screen.Home.route,
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(innerPadding)
    ) {
        composable(Screen.Home.route) { HomePage() }
        composable(Screen.Question.route) { QuestionPage() }
        composable(Screen.System.route) { CategoryPage() }
        composable(Screen.Profile.route) { ProfilePage() }
    }
}

@Composable
private fun BottomNavigation(controller: NavHostController) {
    val backStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val screens = listOf(
        Screen.Home,
        Screen.Question,
        Screen.System,
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
//    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme)
//private object NoRippleTheme : RippleTheme {
//    @Composable
//    override fun defaultColor() = Color.Unspecified
//
//    @Composable
//    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
//}