package com.compose.wanandroid.ui.page.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.wanandroid.ui.theme.AppTheme

const val TAG = "mjw"

@Composable
fun ProfilePage() {
    val viewModel: ProfileViewModel = viewModel()
    TestScreen(viewModel = viewModel, viewState = viewModel.viewStates)
}

@Composable
fun TestScreen(viewModel: ProfileViewModel, viewState: TestViewState) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Log.i(TAG, "TestScreen: recompose all1")

        val state1 = remember {
            mutableStateOf(viewState.state1)
        }
        val state2 = remember {
            mutableStateOf(viewState.state2)
        }
        val state3 = remember {
            mutableStateOf(viewState.state3)
        }
        Button1(state1.value, viewModel)
        Button2(state2.value, viewModel)
        Button3(state3.value, viewModel)
    }
}

@Composable
fun Button1(state: String, viewModel: ProfileViewModel) {
    if (state != "") {
        Log.i(TAG, "TestScreen: state1 recompose")
    } else {
        Log.i(TAG, "TestScreen: state1 recompose2")
    }
    Column {
        Text(text = "state1 $state", color = AppTheme.colors.textPrimary)
        Button(onClick = {
            viewModel.dispatch(TestViewAction.ClickBtn1)
        }) {
            Text(text = "click1", color = AppTheme.colors.textPrimary)
        }
    }
}

@Composable
fun Button2(state: Boolean, viewModel: ProfileViewModel) {
    if (state) {
        Log.i(TAG, "TestScreen: state2 recompose")
    } else {
        Log.i(TAG, "TestScreen: state2 recompose2")
    }
    Column {
        Text(text = "state2 = $state", color = AppTheme.colors.textPrimary)
        Button(onClick = {
            viewModel.dispatch(TestViewAction.ClickBtn2)
        }) {
            Text(text = "click2", color = AppTheme.colors.textPrimary)
        }
    }
}

@Composable
fun Button3(state: Int, viewModel: ProfileViewModel) {
    if (state != 0) {
        Log.i(TAG, "TestScreen: state3 recompose")
    } else {
        Log.i(TAG, "TestScreen: state3 recompose2")
    }

    Text(text = "state3 = $state", color = AppTheme.colors.textPrimary)

    Button(onClick = {
        viewModel.dispatch(TestViewAction.ClickBtn3)
    }) {
        Text(text = "click3", color = AppTheme.colors.textPrimary)
    }
}