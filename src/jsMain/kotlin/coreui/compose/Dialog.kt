/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun Dialog(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable () -> Unit
) {
    Box(
        attrs = {
            style {
                width(100.vw)
                height(100.vh)
                top(0.px)
                left(0.px)
                backgroundColor("rgba(0, 0, 0, 0.3)")
                pointerEvents(PointerEvents.Fill)
            }
        },
        overlapElevation = 100L,
        contentAlignment = Alignment.Box.Center
    ) {
        Surface(
            attrs = {
                applyAttrs(attrs)
            },
            shadowElevation = ShadowElevation.Level3,
            tonalElevation = TonalElevation.Level3
        ) {
            content()
        }
    }
}