package com.compose.wanandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

data class Navigate(
    var articles: MutableList<Article> = mutableListOf(),
    var cid: Int = -1,
    var name: String = ""
)