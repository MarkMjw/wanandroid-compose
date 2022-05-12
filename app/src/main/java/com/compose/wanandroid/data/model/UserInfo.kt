package com.compose.wanandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    var id: Int,
    var admin: Boolean,
    var chapterTops: MutableList<Int>,
    var coinCount: Int,
    var collectIds: MutableList<Int>,
    var email: String,
    var icon: String,
    var nickname: String,
    var password: String,
    var token: String,
    var type: Int,
    var username: String,
) : Parcelable {
    val avatar: String // 找个默认的头像
        get() = icon.ifEmpty { "https://tva1.sinaimg.cn/large/e6c9d24egy1h25egcfp3aj20yq0nvq7i.jpg" }
}

@Parcelize
data class CoinInfo(
    var coinCount: Int = 0,
    var level: Int = 0,
    var rank: Int = 0,
    var userId: Int = 0,
    var username: String = "",
    var nickname: String = ""
) : Parcelable

data class UserResponse(
    var userInfo: UserInfo? = null,
    var coinInfo: CoinInfo? = null
)