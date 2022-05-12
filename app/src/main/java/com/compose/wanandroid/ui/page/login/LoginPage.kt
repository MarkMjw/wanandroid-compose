package com.compose.wanandroid.ui.page.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.R
import com.compose.wanandroid.logic.noRippleClickable
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.AppBarHeight
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.ProgressViewEvent
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.showSnackbar
import com.compose.wanandroid.ui.widget.ProgressDialog
import kotlinx.coroutines.launch

@Preview
@Composable
fun LoginPagePreview() {
    AppTheme {
        LoginPage()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    navController: NavController = rememberNavController(),
    viewModel: LoginViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val viewState = viewModel.viewState
    var showDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf("加载中...") }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            when (it) {
                is LoginViewEvent.Back -> {
                    showDialog = false
                    navController.popBackStack()
                }
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
        topBar = {},
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.primary)
                    .statusBarsPadding()
                    .height(250.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(AppBarHeight),
                    onClick = {
                        keyboardController?.hide()
                        navController.back()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = AppTheme.colors.onPrimary,
                        contentDescription = "Close"
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )

                Text(
                    text = "欢迎使用",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Text(
                    text = "账号服务由WanAndroid提供",
                    fontSize = 12.sp,
                    color = AppTheme.colors.onPrimary.copy(0.8f),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 5.dp),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Input(
                text = viewState.account,
                label = "请输入用户名",
                leadingIcon = R.drawable.ic_account_normal,
                keyboardType = KeyboardType.Email
            ) { account ->
                viewModel.dispatch(LoginViewAction.UpdateAccount(account))
            }

            Input(
                text = viewState.password,
                label = "请输入密码",
                leadingIcon = R.drawable.ic_password_normal,
                keyboardType = KeyboardType.Password,
                isPassword = true
            ) { password ->
                viewModel.dispatch(LoginViewAction.UpdatePassword(password))
            }

            Spacer(modifier = Modifier.height(10.dp))
            var checkedState by remember { mutableStateOf(false) }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp, end = 60.dp)
                    .wrapContentHeight()
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = !checkedState
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppTheme.colors.primary,
                        uncheckedColor = AppTheme.colors.textThird,
                        checkmarkColor = AppTheme.colors.onPrimary,
                    )
                )
                Text(
                    text = "没有账号，注册一个？",
                    fontSize = 12.sp,
                    color = AppTheme.colors.textThird,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
                    .height(40.dp)
                    .background(AppTheme.colors.primary, RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .clickable {
                        viewModel.dispatch(LoginViewAction.Login(checkedState))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (checkedState) "注册" else "登录",
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

@Composable
fun Input(
    text: String,
    label: String = "",
    @DrawableRes leadingIcon: Int = 0,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(!isPassword) }
    var hasFocus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp)
            .wrapContentHeight()
    ) {
        TextField(
            value = text,
            onValueChange = onValueChanged,
            singleLine = true,
            modifier = Modifier
                .wrapContentHeight()
                .onFocusChanged {
                    hasFocus = it.hasFocus
                },
            textStyle = TextStyle(
                color = AppTheme.colors.onBackground,
                fontSize = 15.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            label = {
                Text(
                    text = label,
                    color = AppTheme.colors.textThird,
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(if (hasFocus) AppTheme.colors.highlight else AppTheme.colors.textThird)
                )
            },
            trailingIcon = {
                if (hasFocus && text.trim().isNotEmpty()) {
                    Row {
                        if (isPassword) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_eye_normal),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(if (passwordVisible) AppTheme.colors.highlight else AppTheme.colors.textThird),
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(25.dp)
                                    .noRippleClickable {
                                        passwordVisible = !passwordVisible
                                    },
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(AppTheme.colors.textThird),
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .size(25.dp)
                                .noRippleClickable {
                                    onValueChanged("")
                                }
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = AppTheme.colors.highlight,
                textColor = AppTheme.colors.onBackground,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RectangleShape,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        )

        Divider(
            modifier = Modifier
                .height(0.2.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = AppTheme.colors.textThird
        )
    }
}