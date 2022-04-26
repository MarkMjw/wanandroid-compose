package com.compose.wanandroid.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalShapes = staticCompositionLocalOf {
    Shapes(
        small = RoundedCornerShape(size = 5.dp),
        medium = RoundedCornerShape(size = 10.dp),
        large = RoundedCornerShape(size = 20.dp)
    )
}