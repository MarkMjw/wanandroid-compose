package com.compose.wanandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    var url: String,
    var title: String = ""
) : Parcelable

@Parcelize
data class ArticleTag(
    var name: String,
    var url: String
) : Parcelable

@Parcelize
data class Banner(
    var desc: String = "",
    var id: Int = -1,
    var imagePath: String = "",
    var isVisible: Int = 1,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable

@Parcelize
data class Struct(
    var children: MutableList<Struct> = mutableListOf(),
    var courseId: Int = -1,
    var id: Int = -1,
    var name: String = "",
    var order: Int = -1,
    var parentChapterId: Int = -1,
    var userControlSetTop: Boolean = false,
    var visible: Int = -1,
    var icon: String = "",
    var link: String = ""
) : Parcelable

@Parcelize
data class Navigate(
    var articles: MutableList<Article> = mutableListOf(),
    var cid: Int = -1,
    var name: String = ""
) : Parcelable

data class CollectLink(
    var author: String = "",
    var chapterId: Int = 0,
    var chapterName: String = "",
    var courseId: Int = 0,
    var desc: String = "",
    var envelopePic: String = "",
    var id: Int = 0,
    var link: String = "",
    var niceDate: String = "",
    var origin: String = "",
    var originId: Int = 0,
    var publishTime: Long = 0,
    var title: String = "",
    var userId: Int = 0,
    var visible: Int = 0,
    var zan: Int = 0
)