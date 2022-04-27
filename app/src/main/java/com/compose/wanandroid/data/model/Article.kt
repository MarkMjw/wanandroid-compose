package com.compose.wanandroid.data.model

import android.os.Parcelable
import androidx.core.text.HtmlCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    var apkLink: String = "",
    var audit: Int = -1,
    var author: String = "",
    var canEdit: Boolean = false,
    var chapterId: Int = -1,
    var chapterName: String = "",
    var collect: Boolean = false,
    var courseId: Int = -1,
    var desc: String = "",
    var descMd: String = "",
    var envelopePic: String = "",
    var fresh: Boolean = false,
    var host: String = "",
    var id: Int = -1,
    var link: String = "",
    var niceDate: String = "",
    var niceShareDate: String = "",
    var origin: String = "",
    var prefix: String = "",
    var projectLink: String = "https://www.wanandroid.com",
    var publishTime: Long = 0L,
    var realSuperChapterId: Int = -1,
    var selfVisible: Int = -1,
    var shareDate: Long = 0L,
    var shareUser: String = "",
    var superChapterId: Int = -1,
    var superChapterName: String = "",
    var tags: MutableList<ArticleTag>? = null,
    var title: String = "",
    var type: Int = -1,
    var userId: Int = -1,
    var visible: Int = -1,
    var zan: Int = -1
) : Parcelable

val Article.authorName: String
    get() {
        return when {
            author.isNotEmpty() -> author
            shareUser.isNotEmpty() -> shareUser
            else -> "匿名"
        }
    }

val Article.fixTitle: String
    get() = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()

val Article.chapter: String
    get() = listOf(superChapterName, chapterName).filter { it.isNotEmpty() }.joinToString("·")