package com.compose.wanandroid.data.model

data class Banner(
    var desc: String = "",
    var id: Int,
    var imagePath: String = "",
    var isVisible: Int,
    var order: Int,
    var title: String = "",
    var type: Int,
    var url: String = ""
)