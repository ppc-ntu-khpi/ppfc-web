/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose.base

import androidx.compose.runtime.Composable
import coreui.compose.PointerEvents
import coreui.compose.applyAttrs
import coreui.compose.pointerEvents
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun Box(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    contentAlignment: Alignment.Box = Alignment.Box.TopStart,
    overlapElevation: Long? = null,
    content: @Composable () -> Unit = {}
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                justifyContent(contentAlignment.justifyContent)
                alignItems(contentAlignment.alignItems)
                position(Position.Relative)
                if(overlapElevation != null) {
                    position(Position.Absolute)
                    pointerEvents(PointerEvents.None)
                    property("z-index", overlapElevation)
                }
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}