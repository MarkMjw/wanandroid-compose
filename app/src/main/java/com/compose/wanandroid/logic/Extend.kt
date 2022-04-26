@file:Suppress("Deprecation")

package com.compose.wanandroid.logic

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast

fun Activity.transparentStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    val vis = window.decorView.systemUiVisibility
    window.decorView.systemUiVisibility = option or vis
    window.statusBarColor = Color.TRANSPARENT
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