package com.compose.wanandroid.ui.page.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.R
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.darkMode
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.theme.*
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.ProgressViewEvent
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.showSnackbar
import com.compose.wanandroid.ui.widget.ProgressDialog
import kotlinx.coroutines.launch

@Preview
@Composable
fun SettingPagePreview() {
    AppTheme {
        SettingPage(rememberNavController())
    }
}

@Composable
fun SettingPage(
    navController: NavController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: SettingViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState
    var showDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf("加载中...") }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is SnackViewEvent -> {
                    showDialog = false
                    scope.launch {
                        scaffoldState.showSnackbar(it.message)
                    }
                }
                is ProgressViewEvent -> {
                    progress = it.message
                    showDialog = it.show
                }
            }
        }
    }

    if (showDialog) {
        ProgressDialog(progress) {
            showDialog = false
        }
    }

    AppScaffold(
        title = Screen.Setting.text,
        onBack = { navController.back() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RadioGroup(viewState.darkModes, viewState.darkModes.indexOf(ThemeState.theme.value), title = "暗黑模式") {
                ThemeState.theme.value = it
                scope.launch {
                    darkMode.update(it.name)
                }
            }
            SwitchItem(text = "书签提醒", subText = "打开应用时以通知方式提醒最新添加书签") {
                Logger.w("mjw", "switch:$it")
            }
            SettingItem(text = "关于我们")

            if (viewState.isLogin) {
                Box(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .align(Alignment.CenterHorizontally)
                        .width(200.dp)
                        .height(40.dp)
                        .background(AppTheme.colors.primary, RoundedCornerShape(20.dp))
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable {
                            viewModel.dispatch(SettingViewAction.Logout)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "退出登录",
                        color = AppTheme.colors.onPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.wrapContentSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RadioGroup(
    options: List<Theme>,
    selectIndex: Int = 0,
    title: String = "",
    onSelected: (Theme) -> Unit = {}
) {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(options[selectIndex])
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 5.dp),
            )
        }

        options.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onOptionSelected(item)
                        onSelected(item)
                    }
                    .padding(start = 21.dp, end = 21.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.radioSelected,
                        unselectedColor = AppTheme.colors.radio
                    ),
                    selected = (item == selectedOption),
                    onClick = null
                )

                Text(
                    text = item.name,
                    color = AppTheme.colors.textPrimary,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                )
            }
        }
    }
}

@Composable
fun SwitchItem(
    text: String,
    subText: String = "",
    onChanged: (Boolean) -> Unit = {}
) {
    val checkedState = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = text,
                fontSize = 15.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(end = 10.dp)
            )

            Text(
                text = subText,
                fontSize = 12.sp,
                color = AppTheme.colors.textSecondary,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 5.dp, end = 10.dp)
            )
        }

        Switch(
            checked = checkedState.value,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.primary,
                uncheckedThumbColor = AppTheme.colors.switchThumbUnchecked,
                checkedTrackColor = AppTheme.colors.primary.copy(alpha = 0.35f),
                uncheckedTrackColor = AppTheme.colors.switchTrackUnchecked,
            ),
            onCheckedChange = {
                checkedState.value = it
                onChanged(it)
            }
        )
    }
}

@Composable
fun SettingItem(
    text: String,
    subText: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 10.dp)
                .weight(1f)
        )

        Text(
            text = subText,
            fontSize = 12.sp,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 10.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_enter),
            tint = AppTheme.colors.textThird,
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        )
    }
}