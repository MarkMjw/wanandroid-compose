package com.compose.wanandroid.ui.page.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Link
import com.compose.wanandroid.logic.navigate
import com.compose.wanandroid.ui.page.main.Page
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.textThird

@Preview
@Composable
fun ProfilePagePreview() {
    AppTheme {
        ProfilePage(rememberNavController())
    }
}

@Composable
fun ProfilePage(
    controller: NavController,
    padding: PaddingValues = PaddingValues(),
    viewModel: ProfileViewModel = viewModel()
) {
    val viewState = viewModel.viewState
    val userInfo = viewState.userInfo
    val coinInfo = viewState.coinInfo

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(AppTheme.colors.primary)
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0x22000000), CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        if (!viewState.isLogin) {
                            controller.navigate(Page.Login.route)
                        }
                    },
                model = userInfo?.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Text(
                text = userInfo?.nickname ?: "?????????",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        if (!viewState.isLogin) {
                            controller.navigate(Page.Login.route)
                        }
                    },
                fontSize = 22.sp,
                color = Color.White
            )

            if (coinInfo != null) {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .wrapContentSize(Alignment.Center)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "??????:${coinInfo.level}",
                        fontSize = 12.sp,
                        color = Color(0xaaffffff)
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = "??????:${coinInfo.rank}",
                        fontSize = 12.sp,
                        color = Color(0xaaffffff)
                    )
                }
            }
        }

        ProfileItem(icon = R.drawable.ic_coin, text = "????????????", subText = "${coinInfo?.coinCount ?: ""}") {
            if (viewState.isLogin) {
                controller.navigate(Page.Coin.route, coinInfo?.coinCount ?: 0)
            } else {
                controller.navigate(Page.Login.route)
            }
        }
        ProfileItem(icon = R.drawable.ic_share_article, text = "????????????") {
            if (viewState.isLogin) {
                controller.navigate(Page.MineShare.route)
            } else {
                controller.navigate(Page.Login.route)
            }
        }
        ProfileItem(icon = R.drawable.ic_collect, text = "????????????") {
            if (viewState.isLogin) {
                controller.navigate(Page.Collect.route)
            } else {
                controller.navigate(Page.Login.route)
            }
        }
        ProfileItem(icon = R.drawable.ic_read_later, text = "????????????") {
            controller.navigate(Page.Bookmark.route)
        }
        ProfileItem(icon = R.drawable.ic_read_record, text = "????????????") {
            controller.navigate(Page.History.route)
        }
        ProfileItem(icon = R.drawable.ic_github, text = "????????????") {
            controller.navigate(Page.Opensource.route)
        }
        ProfileItem(icon = R.drawable.ic_about, text = "????????????", subText = "??????????????????~") {
            controller.navigate(Page.Web.route, Link("https://github.com/MarkMjw", "MarkMjw???Github", false))
        }
        ProfileItem(icon = R.drawable.ic_setting, text = "????????????") {
            controller.navigate(Page.Setting.route)
        }
    }
}

@Composable
fun ProfileItem(
    @DrawableRes icon: Int,
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
        Icon(
            painter = painterResource(id = icon),
            tint = AppTheme.colors.highlight,
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        )

        Text(
            text = text,
            fontSize = 15.sp,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp, end = 10.dp)
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