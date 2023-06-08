/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import org.w3c.dom.Element

fun Element.scrollStateListener(onScrollStateChanged: (scrollState: ScrollState) -> Unit) {
    this.addEventListener(
        type = "scroll",
        callback = {
            onScrollStateChanged(this.getScrollState())
        }
    )
}

fun Element.getScrollState(deviation: Double = 0.0): ScrollState {
    val scrollHeight = this.scrollHeight
    val scrollTop = this.scrollTop
    val clientHeight = this.clientHeight
    return when {
        scrollTop <= deviation -> ScrollState.TOP
        scrollTop + clientHeight + deviation >= scrollHeight -> ScrollState.BOTTOM
        else -> ScrollState.INTERMEDIATE
    }
}

enum class ScrollState {
    TOP,
    BOTTOM,
    INTERMEDIATE
}