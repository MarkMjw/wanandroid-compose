package com.compose.wanandroid.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Colors.tab: Color
    get() = if (isLight) Color(0xff999999) else Color(0x54ffffff)

val Colors.icon: Color
    get() = Color(0x54ffffff)

val Colors.tabBackground: Color
    get() = if (isLight) Color.White else Color(0xff181818)

val Colors.highlight: Color
    get() = Color(0xff4282f4)