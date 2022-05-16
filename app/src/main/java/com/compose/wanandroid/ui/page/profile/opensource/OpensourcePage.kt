package com.compose.wanandroid.ui.page.profile.opensource

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.page.main.Page

fun NavGraphBuilder.opensourceGraph(controller: NavController) {
    composable(route = Page.Opensource.route) {
        OpensourcePage(controller)
    }
}

@Composable
fun OpensourcePage(
    controller: NavController,
    viewModel: OpensourceViewModel = viewModel()
) {
    AppScaffold(
        title = "开源项目",
        onBack = { controller.back() },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                viewModel.data.forEach {
                    LinkItem(
                        title = it.title,
                        link = it.desc,
                        modifier = Modifier.clickable {
                            controller.navigate(Page.Web.route, Link(it.url, it.title, false))
                        }
                    )
                }
            }
        }
    }
}