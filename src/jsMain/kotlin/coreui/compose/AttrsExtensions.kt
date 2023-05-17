/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.Element

fun <T : Element> AttrsScope<T>.applyAttrs(
    vararg attrs: AttrBuilderContext<T>?
) {
    attrs.filterNotNull().forEach {
        it()
    }
}