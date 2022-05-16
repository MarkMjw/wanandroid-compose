package com.compose.wanandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    var url: String,
    var title: String = "",
    var addHistory: Boolean = true
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
    var link: String = "",
    var type: Int = TYPE_STRUCT
) : Parcelable {
    companion object {
        const val TYPE_STRUCT = 0
        const val TYPE_ACCOUNT = 1
        const val TYPE_PROJECT = 2
    }
}

@Parcelize
data class Navigate(
    var articles: MutableList<Article> = mutableListOf(),
    var cid: Int = -1,
    var name: String = ""
) : Parcelable

data class CollectLink(
    var desc: String = "",
    var icon: String = "",
    var id: Int = 0,
    var link: String = "",
    var name: String = "",
    var order: Int = 0,
    var userId: Int = 0,
    var visible: Int = 0,
)

data class HotKey(
    val id: Int,
    val link: String = "",
    val name: String = "",
    val order: Int = 1,
    val visible: Int = 1,
)

data class Coin(
    val coinCount: Int = 0,
    val date: Long = 0,
    val desc: String = "",
    val id: Int = 0,
    val type: Int = 0,
    val userId: Int = 0,
    val userName: String = ""
) {
    val title: String
        get() {
            // 2022-05-16 10:23:02 签到 , 积分：10 + 4
            val firstSpace = desc.indexOf(" ")
            val secondSpace = desc.indexOf(" ", firstSpace + 1)
            return desc.substring(secondSpace + 1)
                .replace(" , ", "")
//                .replace("：", "")
//                .replace(" ", "")
        }

    val time: String
        get() {
            val firstSpace = desc.indexOf(" ")
            val secondSpace = desc.indexOf(" ", firstSpace + 1)
            return desc.substring(0, secondSpace)
        }

    val count: String
        get() = if (coinCount < 0) "-$coinCount" else "+$coinCount"
}