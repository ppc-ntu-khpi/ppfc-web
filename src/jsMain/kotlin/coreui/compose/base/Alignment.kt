/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose.base

import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent

interface Alignment {
    sealed class Horizontal(val alignItems: AlignItems) {
        object Start : Horizontal(alignItems = AlignItems.Start)
        object CenterHorizontally : Horizontal(alignItems = AlignItems.Center)
        object End : Horizontal(alignItems = AlignItems.End)
    }

    sealed class Vertical(val alignItems: AlignItems) {
        object Top : Vertical(alignItems = AlignItems.Start)
        object CenterVertically : Vertical(alignItems = AlignItems.Center)
        object Bottom : Vertical(alignItems = AlignItems.End)
    }

    sealed class Box(val alignItems: AlignItems, val justifyContent: JustifyContent) {
        object TopStart : Box(alignItems = AlignItems.Start, justifyContent = JustifyContent.Start)
        object TopCenter : Box(alignItems = AlignItems.Start, justifyContent = JustifyContent.Center)
        object TopEnd : Box(alignItems = AlignItems.Start, justifyContent = JustifyContent.End)
        object CenterStart : Box(alignItems = AlignItems.Center, justifyContent = JustifyContent.Start)
        object Center : Box(alignItems = AlignItems.Center, justifyContent = JustifyContent.Center)
        object CenterEnd : Box(alignItems = AlignItems.Center, justifyContent = JustifyContent.End)
        object BottomStart : Box(alignItems = AlignItems.End, justifyContent = JustifyContent.Start)
        object BottomCenter : Box(alignItems = AlignItems.End, justifyContent = JustifyContent.Center)
        object BottomEnd : Box(alignItems = AlignItems.End, justifyContent = JustifyContent.End)
    }
}