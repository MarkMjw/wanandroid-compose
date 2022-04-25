package com.compose.wanandroid.ui.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.ui.page.home.HomePage
import com.compose.wanandroid.ui.page.profile.ProfilePage
import com.compose.wanandroid.ui.page.question.QuestionPage
import com.compose.wanandroid.ui.page.system.SystemPage
import com.compose.wanandroid.ui.theme.Highlight
import com.compose.wanandroid.ui.theme.TabBackground
import com.compose.wanandroid.ui.theme.TabColor
import com.compose.wanandroid.ui.theme.WanAndroidTheme

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    WanAndroidTheme {
        MainPage()
    }
}

@Composable
fun MainPage() {
    val navController = rememberNavController()
    var selectedTab by remember {
        mutableStateOf(MainTabs.TAB_HOME)
    }

    Scaffold(bottomBar = {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            MainTabs.values().forEach { tab ->
                BottomNavigationItem(
                    selected = tab == selectedTab,
                    icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
                    label = { Text(text = tab.title, fontSize = 11.sp) },
                    onClick = {
                        if (tab != selectedTab) {
                            navController.navigate(tab.route)
                            selectedTab = tab
                        }
                    },
                    alwaysShowLabel = true,
                    selectedContentColor = Highlight,
                    unselectedContentColor = TabColor,
                    modifier = Modifier.background(TabBackground)
                )
            }
        }
    }) {
        NavHost(navController = navController, startDestination = MainTabs.TAB_HOME.route, modifier = Modifier.padding(it)) {
            composable(MainTabs.TAB_HOME.route) { HomePage() }
            composable(MainTabs.TAB_QUESTION.route) { QuestionPage() }
            composable(MainTabs.TAB_SYSTEM.route) { SystemPage() }
            composable(MainTabs.TAB_PROFILE.route) { ProfilePage() }
        }
    }
}