@file:OptIn(ExperimentalPagerApi::class)

package com.compose.wanandroid.ui.widget

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    count: Int,
    loop: Boolean = false,
    timeMillis: Long = 3000,
    direction: Direction = Direction.Horizontal,
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp),
    itemSpacing: Dp = 0.dp,
    reverseLayout: Boolean = false,
    state: BannerState = rememberBannerState(),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable (BannerScope.(page: Int) -> Unit),
) {

    val realCount = if (loop) Int.MAX_VALUE else count
    val startIndex = if (loop) Int.MAX_VALUE / 2 else state.initialPage

    val pagerState = rememberPagerState(initialPage = startIndex)

    val bannerScope = remember(state, startIndex, count) {
        state.pageState = pagerState
        state.showPageCount = count
        state.initialPage = startIndex
        BannerScopeImpl(state)
    }

    LaunchedEffect(loop) {
        pagerState.scrollToPage(startIndex)
    }

    // 监听动画执行
    var executeChangePage by remember { mutableStateOf(false) }
    var currentPageIndex = 0

    // 自动滚动
    LaunchedEffect(pagerState.currentPage, executeChangePage) {
        if (pagerState.pageCount > 0) {
            delay(timeMillis)
            // 这里直接+1就可以循环，前提是infiniteLoop == true
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % (pagerState.pageCount))
        }
    }

    when (direction) {
        is Direction.Horizontal -> {
            HorizontalPager(
                modifier = modifier.pointerInput(pagerState.currentPage) {
                    awaitPointerEventScope {
                        while (true) {
                            // PointerEventPass.Initial - 本控件优先处理手势，处理后再交给子组件
                            val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                            // 获取到第一根按下的手指
                            val dragEvent = event.changes.firstOrNull() ?: return@awaitPointerEventScope
                            when {
                                // 当前移动手势是否已被消费
                                dragEvent.isConsumed -> return@awaitPointerEventScope
                                // 是否已经按下(忽略按下手势已消费标记)
                                dragEvent.changedToDownIgnoreConsumed() -> {
                                    // 记录下当前的页面索引值
                                    currentPageIndex = pagerState.currentPage
                                }
                                // 是否已经抬起(忽略按下手势已消费标记)
                                dragEvent.changedToUpIgnoreConsumed() -> {
                                    // 当pageCount大于1，且手指抬起时如果页面没有改变，就手动触发动画
                                    if (currentPageIndex == pagerState.currentPage && pagerState.pageCount > 1) {
                                        executeChangePage = !executeChangePage
                                    }
                                }
                            }
                        }
                    }
                },
                count = realCount,
                state = pagerState,
                contentPadding = contentPadding,
                itemSpacing = itemSpacing,
                reverseLayout = reverseLayout,
                verticalAlignment = verticalAlignment
            ) { index ->
                val page = if (loop) (index - startIndex).floorMod(count) else index
                bannerScope.content(page)
            }
        }

        is Direction.Vertical -> {
            VerticalPager(
                count = realCount,
                state = pagerState,
                contentPadding = contentPadding,
                itemSpacing = itemSpacing,
                reverseLayout = reverseLayout,
                modifier = modifier,
                horizontalAlignment = horizontalAlignment
            ) { index ->
                val page = if (loop) (index - startIndex).floorMod(count) else index
                bannerScope.content(page)
            }
        }
    }

}

@Stable
interface BannerScope {
    val currentPage: Int
    val currentPageOffset: Float
    val initialPage: Int
    val showPageCount: Int
}

@ExperimentalPagerApi
private class BannerScopeImpl(
    private val state: BannerState,
) : BannerScope {
    override val currentPage: Int get() = state.currentPage
    override val currentPageOffset: Float get() = state.currentPageOffset
    override val initialPage: Int
        get() = state.initialPage
    override val showPageCount: Int
        get() = state.showPageCount
}

@Composable
fun rememberBannerState(
    @IntRange(from = 0) initialPage: Int = 0,
): BannerState = rememberSaveable(saver = BannerState.Saver) {
    BannerState(
        initialPage = initialPage,
    )
}

class BannerState(
    @IntRange(from = 0) var initialPage: Int = 0,
) {

    internal lateinit var pageState: PagerState

    private var _pageCount: Int by mutableStateOf(0)

    val realPageCount: Int
        get() = pageState.pageCount

    val currentPage: Int
        get() = pageState.currentPage

    val currentPageOffset: Float
        get() = pageState.currentPageOffset

    @get:IntRange(from = 0)
    var showPageCount: Int
        get() = _pageCount
        internal set(value) {
            if (value != _pageCount) {
                _pageCount = value
            }
        }

    suspend fun scrollToPage(
        @IntRange(from = 0) page: Int,
        @FloatRange(from = 0.0, to = 1.0) pageOffset: Float = 0f,
    ) {
        pageState.scrollToPage(page, pageOffset)
    }

    suspend fun animateScrollToPage(
        @IntRange(from = 0) page: Int,
        @FloatRange(from = 0.0, to = 1.0) pageOffset: Float = 0f,
    ) {
        pageState.animateScrollToPage(page, pageOffset)
    }

    companion object {
        val Saver: Saver<BannerState, *> = Saver(
            save = {
                it.initialPage
            },
            restore = {
                BannerState(
                    initialPage = it as Int,
                )
            }
        )
    }
}

sealed class Direction {
    object Vertical : Direction()
    object Horizontal : Direction()
}

fun BannerScope.calculateCurrentOffsetForPage(page: Int): Float {
    return abs(currentPage - initialPage - page) % showPageCount + currentPageOffset
}

fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}
