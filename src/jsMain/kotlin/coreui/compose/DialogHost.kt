/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
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
            applyAttrs(attrs)
        }
    ) {
        content(dialog)
    }
}