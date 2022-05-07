package com.compose.wanandroid.ui.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.compose.wanandroid.R
import com.compose.wanandroid.data.model.*
import com.compose.wanandroid.logic.noRippleClickable
import com.compose.wanandroid.logic.rippleClickable
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.AppThemePreview
import com.compose.wanandroid.ui.theme.collectColor
import com.compose.wanandroid.ui.theme.textThird
import com.compose.wanandroid.ui.widget.html.HtmlText

@Preview
@Composable
fun ArticleItemPreview() {
    AppThemePreview {
        Column {
            ArticleItem(
                data = Article().apply {
                    author = "扔物线"
                    title = "属性动画为什么不能移植到 Jetpack Compose？"
                    desc = "基于ViewPager2实现的自动(手动)无限轮播Banner，支持自定义ItemView、横竖滑动、转场动画、配合DiffUtil增量更新等"
                    niceDate = "2020-03-23 16:36"
                    link = "https://rengwuxian.com/jetpack-compose-animation/"
                    publishTime = 1653753600000L
                    chapterName = "干货资源"
                    envelopePic = "https://www.wanandroid.com/resources/image/pc/default_project_img.jpg"
                    fresh = true
                    tags = mutableListOf(ArticleTag("项目", "https://www.wanandroid.com"))
                },
                isTop = true,
            )

            Spacer(modifier = Modifier.height(20.dp))
            StickyTitle(title = "分类标题")
        }
    }
}

@Composable
fun ArticleItem(
    data: Article,
    modifier: Modifier = Modifier,
    isTop: Boolean = false,
    onSelected: (Link) -> Unit = {},
    onCollectClick: (articleId: Int) -> Unit = {},
    onUserClick: (userId: Int) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .rippleClickable {
                onSelected(Link(url = data.link, title = data.title))
            }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (data.fresh) {
                    Text(
                        text = "新",
                        color = AppTheme.colors.secondary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 5.dp)
                    )
                }

                Text(
                    text = data.authorName,
                    color = AppTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentSize()
                        .noRippleClickable {
                            onUserClick(data.userId)
                        }
                )

                val tags = data.tags
                if (!tags.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = tags[0].name,
                        color = AppTheme.colors.secondary,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .border(BorderStroke(1.dp, AppTheme.colors.secondary), RoundedCornerShape(4.dp))
                            .padding(start = 3.dp, end = 3.dp)
                            .wrapContentSize()
                    )
                }

                Text(
                    text = data.niceDate,
                    color = AppTheme.colors.textThird,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                        .wrapContentHeight()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (data.envelopePic.isNotEmpty()) {
                    AsyncImage(
                        model = data.envelopePic,
                        placeholder = painterResource(id = R.drawable.image_holder),
                        modifier = Modifier
                            .size(width = 120.dp, height = 80.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(AppTheme.colors.secondaryBackground),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(IntrinsicSize.Min)
                ) {
                    HtmlText(
                        modifier = Modifier.wrapContentSize(),
                        text = data.title,
                        color = AppTheme.colors.textPrimary,
                        fontSize = 15.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        urlSpanStyle = SpanStyle(textDecoration = null)
                    )

                    if (data.desc.isNotEmpty()) {
                        Spacer(modifier = Modifier.size(5.dp))
                        HtmlText(
                            modifier = Modifier.wrapContentSize(),
                            text = data.desc,
                            color = AppTheme.colors.textSecondary,
                            fontSize = 13.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            urlSpanStyle = SpanStyle(textDecoration = null)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (isTop) {
                    Text(
                        text = "置顶",
                        color = AppTheme.colors.collectColor,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 5.dp)
                    )
                }

                val chapter = data.chapter
                if (chapter.isNotEmpty()) {
                    Text(
                        text = chapter,
                        color = AppTheme.colors.textThird,
                        fontSize = 12.sp,
                        modifier = Modifier.wrapContentSize()
                    )
                }

                val favorite = if (data.collect) R.drawable.icon_favorite else R.drawable.icon_unfavorite
                Image(
                    painter = painterResource(id = favorite),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(AppTheme.colors.collectColor),
                    modifier = Modifier
                        .size(20.dp)
                        .wrapContentWidth(Alignment.End)
                        .weight(1f)
                        .rippleClickable(bounded = false) {
                            onCollectClick(data.id)
                        }
                )
            }
        }
    }
}

@Composable
fun StickyTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(AppTheme.colors.background)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(16.dp)
                .align(Alignment.CenterVertically)
                .background(shape = RoundedCornerShape(3.dp), color = AppTheme.colors.textPrimary)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 16.sp,
            color = AppTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}

