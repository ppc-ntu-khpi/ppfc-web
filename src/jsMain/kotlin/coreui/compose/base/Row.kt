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
fun Row(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Horizontal.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Vertical.Top,
    content: @Composable () -> Unit
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                justifyContent(horizontalArrangement.justifyContent)
                alignItems(verticalAlignment.alignItems)
                boxSizing(BoxSizing.BorderBox)
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}