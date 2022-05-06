package com.compose.wanandroid.ui.page.system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.Banner

@Composable
fun SystemPage() {
    val urls = listOf(
        Banner(imagePath = "https://wx2.sinaimg.cn/mw2000/77fc0a91ly1h1xe7hid0xj24hr2zuqv6.jpg", title = "图一"),
        Banner(imagePath = "https://wx3.sinaimg.cn/mw2000/77fc0a91ly1h1xe6wjl8hj24fd2y9kjl.jpg", title = "图二"),
        Banner(imagePath = "https://wx4.sinaimg.cn/mw2000/77fc0a91ly1h1xe7pp3w7j24ep2xtkjl.jpg", title = "图三"),
        Banner(imagePath = "https://wx2.sinaimg.cn/mw2000/77fc0a91ly1h1xe7qqib6j24gl2z27wh.jpg", title = "图四"),
    )
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.78f)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.78f)
                ) {
                    items(urls) { banner ->
                        AsyncImage(
                            model = banner.imagePath,
                            placeholder = painterResource(id = R.drawable.image_holder),
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        items(20) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
                text = "店里来了一台孤傲的 限少数人且不可一世的 718 spyder 施工卡钳改色\uD83E\uDD13 这玩意是不是跟国外的版本略有区别[doge]",
                color = AppTheme.colors.textPrimary,
                fontSize = 14.sp,
            )
        }
    }
}