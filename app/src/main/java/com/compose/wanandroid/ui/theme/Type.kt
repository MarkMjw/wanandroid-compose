package com.compose.wanandroid.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    val title1: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    val title2: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    val body1: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    val body1Bold: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    val body2: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 18.sp
    ),
    val body2Bold: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 18.sp
    ),
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val captionBold: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }