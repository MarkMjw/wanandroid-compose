package com.compose.wanandroid.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse

// material dark
//val primary: Color = Color(0xFFBB86FC)
//val primaryVariant: Color = Color(0xFF3700B3)
//val secondary: Color = Color(0xFF03DAC6)
//val secondaryVariant: Color = secondary
//val background: Color = Color(0xFF121212)
//val surface: Color = Color(0xFF121212)
//val error: Color = Color(0xFFCF6679)
//val onPrimary: Color = Color.Black
//val onSecondary: Color = Color.Black
//val onBackground: Color = Color.White
//val onSurface: Color = Color.White
//val onError: Color = Color.Black

// material light
//val primary: Color = Color(0xFF6200EE)
//val primaryVariant: Color = Color(0xFF3700B3)
//val secondary: Color = Color(0xFF03DAC6)
//val secondaryVariant: Color = Color(0xFF018786)
//val background: Color = Color.White
//val surface: Color = Color.White
//val error: Color = Color(0xFFB00020)
//val onPrimary: Color = Color.White
//val onSecondary: Color = Color.Black
//val onBackground: Color = Color.Black
//val onSurface: Color = Color.Black
//val onError: Color = Color.White

val LightColors = AppColors(
    primary = Color(0xff3651f1),
    secondary = Color(0xff4282f4),
    textPrimary = Color(0xff333333),
    textSecondary = Color(0xff666666),
    background = Color.White,
    secondaryBackground = Color(0xfff5f5f5),
    highlight = Color(0xff4282f4),
    error = Color(0xffb00020),
    onPrimary = Color.White,
    onBackground = Color(0xff333333),
    isLight = true
)

val DarkColors = AppColors(
    primary = Color(0xff3651f1),
    secondary = Color(0xff4282f4),
    textPrimary = Color(0xffcfcfd1),
    textSecondary = Color(0x88ffffff),
    background = Color(0xff121212),
    secondaryBackground = Color(0xff181818),
    highlight = Color(0xff4282f4),
    error = Color(0xffcf6679),
    onPrimary = Color(0xff333333),
    onBackground = Color(0xffcfcfd1),
    isLight = false
)

val AppColors.collectColor: Color
    get() = if (isLight) Color(0xfff78c65) else Color(0xfff86734)

val AppColors.textThird: Color
    get() = if (isLight) Color(0xff999999) else Color(0x54ffffff)

@Composable
fun AppTheme(
    typography: AppTypography = AppTheme.typography,
    shapes: Shapes = AppTheme.shapes,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    // creating a new object for colors to not mutate the initial colors set when updating the values
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    val rippleIndication = rememberRipple()

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalShapes provides shapes,
        LocalTypography provides typography,
        LocalIndication provides rippleIndication,
        LocalRippleTheme provides MaterialRippleTheme,
    ) {
        ProvideTextStyle(value = typography.body1, content = content)
    }

    MaterialTheme
}

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = AppTheme.colors.background,
    contentColor: Color = defaultContentColorFor(color),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = shape,
        content = content
    )
}

object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    // We use the default material shapes
    val shapes: Shapes
        @ReadOnlyComposable
        @Composable
        get() = LocalShapes.current
}

@Immutable
private object MaterialRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = AppTheme.colors.isLight
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = AppTheme.colors.isLight
    )
}

@Composable
@ReadOnlyComposable
fun defaultContentColorFor(backgroundColor: Color): Color =
    AppTheme.colors.contentColorFor(backgroundColor).takeOrElse { LocalContentColor.current }

private fun AppColors.contentColorFor(backgroundColor: Color): Color {
    return when (backgroundColor) {
        primary -> onPrimary
        secondary -> onPrimary
        background -> onBackground
        secondaryBackground -> onBackground
        else -> Color.Unspecified
    }
}