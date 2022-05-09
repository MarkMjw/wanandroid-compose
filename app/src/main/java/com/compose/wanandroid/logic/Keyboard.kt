@file:Suppress("Unused", "Deprecation")

package com.compose.wanandroid.logic

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlin.math.min

//import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
 * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
 *
 * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
 * Api21下调用无效
 *
 * @param float 需要悬浮在软键盘之上的视图
 * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
 * @param editText 需要监听的EditText, 默认监听所有EditText
 * @param margin 悬浮视图和软键盘间距
 * @param onChanged 监听软键盘是否显示
 *
 * @see getSoftInputHeight 软键盘高度
 */
@JvmOverloads
fun Activity.setWindowSoftInput(
    float: View? = null,
    transition: View? = float?.parent as? View,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) = window.setWindowSoftInput(float, transition, editText, margin, onChanged)

/**
 * 如果Fragment不是立即创建, 请为Fragment所在的Activity配置[[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]]
 *
 * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
 * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
 *
 * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
 * Api21下调用无效
 *
 * @param float 需要悬浮在软键盘之上的视图
 * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
 * @param editText 需要监听的EditText, 默认监听所有EditText
 * @param margin 悬浮视图和软键盘间距
 * @param onChanged 监听软键盘是否显示
 *
 * @see getSoftInputHeight 软键盘高度
 */
@JvmOverloads
fun Fragment.setWindowSoftInput(
    float: View? = null,
    transition: View? = view,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) = requireActivity().window.setWindowSoftInput(float, transition, editText, margin, onChanged)

/**
 * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
 * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
 *
 * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
 * Api21下调用无效
 *
 * @param float 需要悬浮在软键盘之上的视图
 * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
 * @param editText 需要监听的EditText, 默认监听所有EditText
 * @param margin 悬浮视图和软键盘间距
 * @param onChanged 监听软键盘是否显示
 *
 * @see getSoftInputHeight 软键盘高度
 */
@JvmOverloads
fun DialogFragment.setWindowSoftInput(
    float: View? = null,
    transition: View? = view,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) = dialog?.window?.setWindowSoftInput(float, transition, editText, margin, onChanged)

///**
// * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
// * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
// *
// * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
// * Api21下调用无效
// *
// * @param float 需要悬浮在软键盘之上的视图
// * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
// * @param editText 需要监听的EditText, 默认监听所有EditText
// * @param margin 悬浮视图和软键盘间距
// * @param onChanged 监听软键盘是否显示
// *
// * @see getSoftInputHeight 软键盘高度
// */
//@JvmOverloads
//fun BottomSheetDialogFragment.setWindowSoftInput(
//    float: View? = null,
//    transition: View? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet),
//    editText: EditText? = null,
//    margin: Int = 0,
//    onChanged: (() -> Unit)? = null,
//) = dialog?.window?.setWindowSoftInput(float, transition, editText, margin, onChanged)

/**
 * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
 * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
 *
 * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
 * Api21下调用无效
 *
 * @param float 需要悬浮在软键盘之上的视图
 * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
 * @param editText 需要监听的EditText, 默认监听所有EditText
 * @param margin 悬浮视图和软键盘间距
 * @param onChanged 监听软键盘是否显示
 *
 * @see getSoftInputHeight 软键盘高度
 */
@JvmOverloads
fun Dialog.setWindowSoftInput(
    float: View? = null,
    transition: View? = window?.decorView,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) = window?.setWindowSoftInput(float, transition, editText, margin, onChanged)

/**
 * 软键盘弹出后要求指定视图[float]悬浮在软键盘之上
 * 本方法重复调用会互相覆盖, 例如Fragment调用会覆盖其Activity的调用
 *
 * Api21以上本方法使用[WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING]
 * Api21下调用无效
 *
 * @param float 需要悬浮在软键盘之上的视图
 * @param transition 当软键盘显示隐藏时需要移动的视图, 使用[View.setTranslationY]移动
 * @param editText 需要监听的EditText, 默认监听所有EditText
 * @param margin 悬浮视图和软键盘间距
 * @param onChanged 监听软键盘是否显示
 *
 * @see getSoftInputHeight 软键盘高度
 */
@JvmOverloads
fun Window.setWindowSoftInput(
    float: View? = null,
    transition: View? = null,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) {
    // 部分系统不支持WindowInsets使用兼容方案处理
    if (!decorView.isSystemInsetsAnimationSupport()) {
        return setWindowSoftInputCompatible(float, transition, editText, margin, onChanged)
    }
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    var matchEditText = false
    var hasSoftInput = false
    var floatInitialBottom = 0
    var startAnimation: WindowInsetsAnimationCompat? = null
    var transitionY = 0f
    val callback = object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

        override fun onStart(
            animation: WindowInsetsAnimationCompat,
            bounds: WindowInsetsAnimationCompat.BoundsCompat
        ): WindowInsetsAnimationCompat.BoundsCompat {
            if (float == null || transition == null) return bounds
            hasSoftInput = ViewCompat.getRootWindowInsets(decorView)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            startAnimation = animation
            if (hasSoftInput) matchEditText = editText == null || editText.hasFocus()
            if (hasSoftInput) {
                floatInitialBottom = run {
                    val r = IntArray(2)
                    float.getLocationInWindow(r)
                    r[1] + float.height
                }
            }
            return bounds
        }

        override fun onEnd(animation: WindowInsetsAnimationCompat) {
            super.onEnd(animation)
            if (matchEditText) onChanged?.invoke()
        }

        override fun onProgress(
            insets: WindowInsetsCompat,
            runningAnimations: MutableList<WindowInsetsAnimationCompat>
        ): WindowInsetsCompat {
            val fraction = startAnimation?.fraction
            if (fraction == null || float == null || transition == null || !matchEditText) return insets
            val softInputHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val softInputTop = decorView.bottom - softInputHeight
            val offset = (softInputTop - floatInitialBottom - margin).toFloat()
            if (hasSoftInput && softInputTop < floatInitialBottom) {
                transition.translationY = offset
                transitionY = transition.translationY
            } else if (!hasSoftInput) {
                transition.translationY = min(transitionY - transitionY * (fraction + 0.5f), 0f)
            }
            return insets
        }
    }
    ViewCompat.setWindowInsetsAnimationCallback(decorView, callback)
}

/** 部分系统不支持WindowInsets使用兼容方案处理 */
private fun Window.setWindowSoftInputCompatible(
    float: View? = null,
    transition: View? = float?.parent as? View,
    editText: EditText? = null,
    margin: Int = 0,
    onChanged: (() -> Unit)? = null,
) {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    var shown = false
    var matchEditText = false
    decorView.viewTreeObserver.addOnGlobalLayoutListener {
        val canTransition = float != null && transition != null
        val floatBottom = if (canTransition) {
            val r = IntArray(2)
            float!!.getLocationInWindow(r)
            r[1] + float.height
        } else 0
        val decorBottom = decorView.bottom
        val rootWindowInsets = ViewCompat.getRootWindowInsets(decorView) ?: return@addOnGlobalLayoutListener
        val softInputHeight = rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        val hasSoftInput = rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
        val offset = (decorBottom - floatBottom - softInputHeight - margin).toFloat()
        if (hasSoftInput) {
            matchEditText = editText == null || editText.hasFocus()
            if (!shown && matchEditText) {
                transition?.translationY = offset
                onChanged?.invoke()
            }
            shown = true
        } else {
            if (shown && matchEditText) {
                transition?.translationY = 0f
                onChanged?.invoke()
            }
            shown = false
        }
    }
}


private var isSupportInsetsFiled: Boolean? = null

/** 判断系统是否支持[WindowInsetsAnimationCompat] */
fun View.isSystemInsetsAnimationSupport(): Boolean {
    var supportInsets = isSupportInsetsFiled
    if (supportInsets == null) {
        supportInsets = ViewCompat.getWindowInsetsController(this) != null
        isSupportInsetsFiled = supportInsets
    }
    return supportInsets
}

/**
 * 弹出软键盘
 * 如果要求页面显示立刻弹出软键盘，建议在onResume方法中调用
 */
fun EditText.showSoftInput() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    if (isSystemInsetsAnimationSupport()) {
        ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())
    } else {
        postDelayed({
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, 0)
        }, 300)
    }
}

/** 隐藏软键盘 */
fun Activity.hideSoftInput() {
    currentFocus?.let {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } ?: let {
        ViewCompat.getWindowInsetsController(window.decorView)?.hide(WindowInsetsCompat.Type.ime())
    }
}

/** 隐藏软键盘 */
fun Fragment.hideSoftInput() = requireActivity().hideSoftInput()

/** 隐藏软键盘 */
fun EditText.hideSoftInput() {
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
}

/** 软键盘是否显示 */
fun Activity.hasSoftInput(): Boolean {
    return ViewCompat.getRootWindowInsets(window.decorView)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
}

/** 软键盘是否显示 */
fun Fragment.hasSoftInput(): Boolean {
    return requireActivity().hasSoftInput()
}

/** 当前软键盘显示高度 */
fun Activity.getSoftInputHeight(): Int {
    val softInputHeight = ViewCompat.getRootWindowInsets(window.decorView)?.getInsets(WindowInsetsCompat.Type.ime())?.bottom
    return softInputHeight ?: 0
}

/** 当前软键盘显示高度 */
fun Fragment.getSoftInputHeight(): Int {
    return requireActivity().getSoftInputHeight()
}