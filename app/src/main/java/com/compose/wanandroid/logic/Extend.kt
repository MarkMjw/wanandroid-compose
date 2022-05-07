@file:Suppress("Deprecation")

package com.compose.wanandroid.logic

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import java.util.regex.Pattern

val Activity.screenSize: Size
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Size(windowManager.currentWindowMetrics.bounds.width(), windowManager.currentWindowMetrics.bounds.height())
        } else {
            Size(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        }
    }

fun Context.isNetConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun CharSequence.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    if (!isNullOrEmpty()) {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            Toast.makeText(context, this, duration).show()
        } else {
            Handler(context.mainLooper).post { Toast.makeText(context, this@toast, duration).show() }
        }
    }
}

fun String.removeAllBank(count: Int): String {
    var s = ""
    if (isNotEmpty()) {
        val p = Pattern.compile("\\s{$count,}|\t|\r|\n");
        val m = p.matcher(this)
        s = m.replaceAll(" ");
    }
    return s
}

inline fun Modifier.rippleClickable(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = rememberRipple(bounded, radius, color),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}