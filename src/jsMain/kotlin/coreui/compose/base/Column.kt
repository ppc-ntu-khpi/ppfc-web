/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose.base

import androidx.compose.runtime.Composable
import coreui.compose.BoxSizing
import coreui.compose.applyAttrs
import coreui.compose.boxSizing
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun Column(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Vertical.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Horizontal.Start,
    content: @Composable () -> Unit
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                justifyContent(verticalArrangement.justifyContent)
                alignItems(horizontalAlignment.alignItems)
                boxSizing(BoxSizing.BorderBox)
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}