/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun <T> DialogHost(
    dialog: T?,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable (T) -> Unit
) {
    dialog ?: return

    Dialog(
        attrs = {
            style {
                maxWidth(100.vw - (20.px * 2))
                maxHeight(100.vh - (20.px * 2))
                overflow(Overflow.Auto)
            }

            applyAttrs(attrs)
        }
    ) {
        content(dialog)
    }
}