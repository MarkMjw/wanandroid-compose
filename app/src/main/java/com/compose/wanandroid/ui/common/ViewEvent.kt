package com.compose.wanandroid.ui.common

interface ViewEvent

data class SnackViewEvent(val message: String) : ViewEvent
data class ProgressViewEvent(val show: Boolean, val message: String = "") : ViewEvent