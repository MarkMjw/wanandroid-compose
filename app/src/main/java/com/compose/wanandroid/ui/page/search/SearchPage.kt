package com.compose.wanandroid.ui.page.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.compose.wanandroid.R
import com.compose.wanandroid.logic.back
import com.compose.wanandroid.logic.noRippleClickable
import com.compose.wanandroid.ui.common.AppScaffold
import com.compose.wanandroid.ui.common.AppTitleBar
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.widget.AppBarHeight

fun NavGraphBuilder.searchGraph(controller: NavController) {
    composable(Page.Search.route) {
        SearchPage(controller)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchPage(controller: NavController) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    AppScaffold(
        topBar = {
            AppTitleBar(
                onBack = {
                    keyboardController?.hide()
                    controller.back()
                },
                title = {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        text = text,
                        onValueChanged = {
                            text = it
                        },
                        hint = "用空格分开多个关键词"
                    )
                },
                trailingActions = {
                    IconButton(
                        onClick = {
                            // TODO search
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                tint = AppTheme.colors.onPrimary,
                                contentDescription = null
                            )
                        }
                    )
                })
        }
    ) {

    }
}

@Composable
fun SearchBar(
    text: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = ""
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .background(Color.Black.copy(0.08f), RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChanged,
            singleLine = true,
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp)
                .wrapContentHeight()
                .weight(1f),
            textStyle = TextStyle(
                color = AppTheme.colors.onPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            cursorBrush = SolidColor(AppTheme.colors.onPrimary),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(all = 0.dp),
                            text = hint,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = AppTheme.colors.onPrimary.copy(0.6f),
                        )
                    }
                    innerTextField()
                }
            }
        )

        AnimatedVisibility(
            visible = text.trim().isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary),
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
                    .noRippleClickable {
                        onValueChanged("")
                    }
            )
        }
    }
}