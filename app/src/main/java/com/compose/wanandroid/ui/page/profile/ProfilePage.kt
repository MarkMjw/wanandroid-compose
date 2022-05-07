package com.compose.wanandroid.ui.page.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.compose.wanandroid.R
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.AppThemePreview
import com.compose.wanandroid.ui.theme.textThird

@Preview
@Composable
fun ProfilePagePreview() {
    AppThemePreview {
        ProfilePage()
    }
}

@Composable
fun ProfilePage(viewModel: ProfileViewModel = viewModel()) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(AppTheme.colors.secondary)
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
                model = "",
                contentDescription = null,
                error = ColorPainter(Color(0x22000000)),
                placeholder = ColorPainter(Color(0x22000000))
            )

            Text(
                text = "未登录",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 22.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "等级:52",
                    fontSize = 12.sp,
                    color = Color(0xaaffffff)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "排名:15",
                    fontSize = 12.sp,
                    color = Color(0xaaffffff)
                )
            }
        }

        ProfileItem(icon = R.drawable.ic_coin, text = "我的积分", subText = "520") { }
        ProfileItem(icon = R.drawable.ic_share_article, text = "我的分享") { }
        ProfileItem(icon = R.drawable.ic_collect, text = "我的收藏") { }
        ProfileItem(icon = R.drawable.ic_read_later, text = "我的书签") { }
        ProfileItem(icon = R.drawable.ic_read_record, text = "阅读历史") { }
        ProfileItem(icon = R.drawable.ic_github, text = "开源项目") { }
        ProfileItem(icon = R.drawable.ic_about, text = "关于作者", subText = "请他喝杯☕️~") { }
        ProfileItem(icon = R.drawable.ic_setting, text = "系统设置") { }
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