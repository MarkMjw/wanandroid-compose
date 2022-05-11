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
) : Parcelable

data class CoinInfo(
    var coinCount: Int = 0,
    var level: Int = 0,
    var rank: Int = 0,
    var userId: Int = 0,
    var username: String = "",
    var nickname: String = ""
)

data class UserResponse(
    var userInfo: UserInfo? = null,
    var coinInfo: CoinInfo? = null
)