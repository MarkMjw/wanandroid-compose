package com.compose.wanandroid.ui.page.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.R
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.ui.page.main.Screen
import com.compose.wanandroid.ui.theme.*
import com.compose.wanandroid.ui.widget.CenterAppBar

@Preview
@Composable
fun SettingPagePreview() {
    AppTheme {
        SettingPage(rememberNavController())
    }
}

@Composable
fun SettingPage(navController: NavController) {
    val darkModes: List<Theme> = listOf(Theme.FollowSystem, Theme.Light, Theme.Dark)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAppBar(
            modifier = Modifier.fillMaxWidth(),
            leftActions = {
                androidx.compose.material.IconButton(onClick = {
                    navController.back()
                }) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = AppTheme.colors.onPrimary,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Text(
                    text = Screen.Setting.text,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.onPrimary
                )
            },
            backgroundColor = AppTheme.colors.primary
        )

        RadioGroup(darkModes, darkModes.indexOf(ThemeState.theme.value), title = "设置暗黑模式") {
            ThemeState.theme.value = it
        }
        SwitchItem(text = "书签提醒", subText = "打开应用时以通知方式提醒最新添加书签") {
            Logger.w("mjw", "switch:$it")
        }
        SettingItem(text = "关于我们")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                checkedTrackColor = AppTheme.colors.secondary.copy(alpha = 0.35f),
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