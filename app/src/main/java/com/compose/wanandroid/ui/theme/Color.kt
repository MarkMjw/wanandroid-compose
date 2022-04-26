package com.compose.wanandroid.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

class AppColors(
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    background: Color,
    secondaryBackground: Color,
    highlight: Color,
    error: Color,
    onPrimary: Color,
    onBackground: Color,
    isLight: Boolean
) {

    var primary by mutableStateOf(primary)
        private set

    var secondary by mutableStateOf(secondary)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    var textSecondary by mutableStateOf(textSecondary)
        private set

    var background by mutableStateOf(background)
        private set

    var secondaryBackground by mutableStateOf(secondaryBackground)
        private set

    var highlight by mutableStateOf(highlight)
        private set

    var error by mutableStateOf(error)
        private set

    var onPrimary by mutableStateOf(onPrimary)
        private set

    var onBackground by mutableStateOf(onBackground)
        private set

    var isLight by mutableStateOf(isLight)
        private set

    fun copy(
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        background: Color = this.background,
        secondaryBackground: Color = this.secondaryBackground,
        highlight: Color = this.highlight,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onBackground: Color = this.onBackground,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        primary,
        secondary,
        textPrimary,
        textSecondary,
        background,
        secondaryBackground,
        highlight,
        error,
        onPrimary,
        onBackground,
        isLight
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        secondary = other.secondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        background = other.background
        secondaryBackground = other.secondaryBackground
        highlight = other.highlight
        error = other.error
        onPrimary = other.onPrimary
        onBackground = other.onBackground
        isLight = other.isLight
    }
}

internal val LocalColors = staticCompositionLocalOf { LightColors }