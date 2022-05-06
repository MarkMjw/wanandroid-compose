@file:Suppress("Unchecked_cast", "Deprecation")

package com.compose.wanandroid.logic

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Size
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.compose.wanandroid.R

/**
 * 设置浅色状态栏背景（文字为深色）
 *
 * @param isLightingColor
 */
fun Activity.setLightStatusBar(isLightingColor: Boolean) {
    val window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isLightingColor) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}

/**
 * 设置浅色导航栏背景（文字为深色）
 *
 * @param isLightingColor
 */
fun Activity.setLightNavigationBar(isLightingColor: Boolean) {
    val window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightingColor) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0
    }
}

/**
 * 沉浸式状态栏，必须在Activity的onCreate时调用
 */
fun Activity.immersiveStatusBar() {
    val view = (window.decorView as ViewGroup).getChildAt(0)
    view.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        val lp = view.layoutParams as FrameLayout.LayoutParams
        if (lp.topMargin > 0) {
            lp.topMargin = 0
            v.layoutParams = lp
        }
        if (view.paddingTop > 0) {
            view.setPadding(0, 0, 0, view.paddingBottom)
            val content = findViewById<View>(android.R.id.content)
            content.requestLayout()
        }
    }

    val content = findViewById<View>(android.R.id.content)
    content.setPadding(0, 0, 0, content.paddingBottom)

    window.decorView.findViewById(R.id.status_bar_view) ?: View(window.context).apply {
        id = R.id.status_bar_view
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusHeight)
        params.gravity = Gravity.TOP
        layoutParams = params
        (window.decorView as ViewGroup).addView(this)

        (window.decorView as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (child?.id == android.R.id.statusBarBackground) {
                    child.scaleX = 0f
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
            }
        })
    }
    setStatusBarColor(Color.TRANSPARENT)
}

/**
 * 沉浸式导航栏，必须在Activity的onCreate时调用
 *
 * @param callback
 */
fun Activity.immersiveNavigationBar(callback: (() -> Unit)? = null) {
    val view = (window.decorView as ViewGroup).getChildAt(0)
    view.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        val lp = view.layoutParams as FrameLayout.LayoutParams
        if (lp.bottomMargin > 0) {
            lp.bottomMargin = 0
            v.layoutParams = lp
        }
        if (view.paddingBottom > 0) {
            view.setPadding(0, view.paddingTop, 0, 0)
            val content = findViewById<View>(android.R.id.content)
            content.requestLayout()
        }
    }

    val content = findViewById<View>(android.R.id.content)
    content.setPadding(0, content.paddingTop, 0, -1)

    val heightLiveData = MutableLiveData<Int>()
    heightLiveData.value = 0
    window.decorView.setTag(R.id.navigation_height_live_data, heightLiveData)
    callback?.invoke()

    window.decorView.findViewById(R.id.navigation_bar_view) ?: View(window.context).apply {
        id = R.id.navigation_bar_view
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, heightLiveData.value ?: 0)
        params.gravity = Gravity.BOTTOM
        layoutParams = params
        (window.decorView as ViewGroup).addView(this)

        if (this@immersiveNavigationBar is FragmentActivity) {
            heightLiveData.observe(this@immersiveNavigationBar) {
                val lp = layoutParams
                lp.height = heightLiveData.value ?: 0
                layoutParams = lp
            }
        }

        (window.decorView as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (child?.id == android.R.id.navigationBarBackground) {
                    child.scaleX = 0f
                    bringToFront()

                    child.addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
                        heightLiveData.value = bottom - top
                    }
                } else if (child?.id == android.R.id.statusBarBackground) {
                    child.scaleX = 0f
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
            }
        })
    }
    setNavigationBarColor(Color.TRANSPARENT)
}

/**
 * 当设置了[immersiveStatusBar]时，如需使用状态栏，可调用该函数
 *
 * @param fit
 */
fun Activity.fitStatusBar(fit: Boolean) {
    val content = findViewById<View>(android.R.id.content)
    if (fit) {
        content.setPadding(0, statusHeight, 0, content.paddingBottom)
    } else {
        content.setPadding(0, 0, 0, content.paddingBottom)
    }
}

/**
 * 当设置了[immersiveNavigationBar]时，如需使用导航栏，可调用该函数
 *
 * @param fit
 */
fun Activity.fitNavigationBar(fit: Boolean) {
    val content = findViewById<View>(android.R.id.content)
    if (fit) {
        content.setPadding(0, content.paddingTop, 0, navigationBarHeightLiveData.value ?: 0)
    } else {
        content.setPadding(0, content.paddingTop, 0, -1)
    }
    if (this is FragmentActivity) {
        navigationBarHeightLiveData.observe(this) {
            if (content.paddingBottom != -1) {
                content.setPadding(0, content.paddingTop, 0, it)
            }
        }
    }
}

val Activity.isImmersiveNavigationBar: Boolean
    get() = window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION != 0

val Activity.statusHeight: Int
    get() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

val Activity.navigationHeight: Int
    get() = navigationBarHeightLiveData.value ?: 0

val Activity.screenSize: Size
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Size(windowManager.currentWindowMetrics.bounds.width(), windowManager.currentWindowMetrics.bounds.height())
        } else {
            Size(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        }
    }

/**
 * 设置状态栏背景色
 *
 * @param color
 */
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    val statusBarView = window.decorView.findViewById<View?>(R.id.status_bar_view)
    if (color == 0 && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        statusBarView?.setBackgroundColor(STATUS_BAR_MASK_COLOR)
    } else {
        statusBarView?.setBackgroundColor(color)
    }
}

/**
 * 设置导航栏栏背景色
 *
 * @param color
 */
fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    val navigationBarView = window.decorView.findViewById<View?>(R.id.navigation_bar_view)
    if (color == 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
        navigationBarView?.setBackgroundColor(STATUS_BAR_MASK_COLOR)
    } else {
        navigationBarView?.setBackgroundColor(color)
    }
}

val Activity.navigationBarHeightLiveData: LiveData<Int>
    get() {
        var liveData = window.decorView.getTag(R.id.navigation_height_live_data) as? LiveData<Int>
        if (liveData == null) {
            liveData = MutableLiveData()
            window.decorView.setTag(R.id.navigation_height_live_data, liveData)
        }
        return liveData
    }

fun Activity.hideStatusBar() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun Activity.showStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

val Activity.screenWidth: Int get() = screenSize.width

val Activity.screenHeight: Int get() = screenSize.height

internal const val STATUS_BAR_MASK_COLOR = 0x7F000000