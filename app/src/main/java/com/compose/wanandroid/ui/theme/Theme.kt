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
    primary = Color(0xff3cdc86),
    secondary = Color(0xff67db9d),
    textPrimary = Color(0xff333333),
    textSecondary = Color(0xff666666),
    background = Color.White,
    secondaryBackground = Color(0xfff5f5f5),
    highlight = Color(0xff3cdc86),
    error = Color(0xffb00020),
    onPrimary = Color.White,
    onBackground = Color(0xff333333),
    isLight = true
)

val DarkColors = AppColors(
    primary = Color(0xff3cdc86),
    secondary = Color(0xff67db9d),
    textPrimary = Color(0xffcfcfd1),
    textSecondary = Color(0x88ffffff),
    background = Color(0xff121212),
    secondaryBackground = Color(0xff222226),
    highlight = Color(0xff3cdc86),
    error = Color(0xffcf6679),
    onPrimary = Color.White,
    onBackground = Color(0xffcfcfd1),
    isLight = false
)

val AppColors.collectColor: Color
    get() = if (isLight) Color(0xfff78c65) else Color(0xfff86734)

val AppColors.textThird: Color
    get() = if (isLight) Color(0xff999999) else Color(0x54ffffff)

val AppColors.tabBackground: Color
    get() = if (isLight) Color.White else Color(0xff181818)

val AppColors.progress: Color
    get() = if (isLight) Color(0xff3cdc86) else Color(0xff67db9d)

val AppColors.radioSelected: Color
    get() = if (isLight) Color(0xff3cdc86) else Color(0xff67db9d)

val AppColors.radio: Color
    get() = if (isLight) Color(0xff666666) else Color(0x88ffffff)

val AppColors.switchThumbUnchecked: Color
    get() = if (isLight) Color(0xffececec) else Color(0xff3B3B3B)

val AppColors.switchTrackUnchecked: Color
    get() = if (isLight) Color(0xffb2b2b2) else Color(0xff242424)


object ThemeState {
    var theme: MutableState<Theme?> = mutableStateOf(null)

    fun read(name: String) {
        theme.value = when (name) {
            Theme.Light.name -> Theme.Light
            Theme.Dark.name -> Theme.Dark
            else -> Theme.FollowSystem
        }
    }
}

sealed class Theme(val name: String) {
    object FollowSystem : Theme("跟随系统")
    object Light : Theme("关闭")
    object Dark : Theme("打开")
}

@Composable
fun AppTheme(
    typography: AppTypography = AppTheme.typography,
    shapes: Shapes = AppTheme.shapes,
    theme: Theme = ThemeState.theme.value ?: Theme.Dark,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        is Theme.Light -> LightColors
        is Theme.Dark -> DarkColors
        is Theme.FollowSystem -> {
            if (isSystemInDarkTheme()) {
                DarkColors
            } else {
                LightColors
            }
        }
    }

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