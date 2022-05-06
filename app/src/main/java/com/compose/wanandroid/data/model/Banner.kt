package com.compose.wanandroid.data.model

data class Banner(
    var desc: String = "",
    var id: Int = -1,
    var imagePath: String = "",
    var isVisible: Int = 1,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
)