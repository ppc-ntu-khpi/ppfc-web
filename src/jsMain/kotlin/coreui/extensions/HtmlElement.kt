/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.extensions

import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.Color
import org.w3c.dom.Element

fun AttrsScope<Element>.elementContext(
    context: (element: Element) -> Unit
) {
    ref { element ->
        context(element)
        onDispose { }
    }
}

fun Element.getActualBackgroundColor(searchDepth: Int = 25): String {
    var currentElement: Element = this.parentElement ?: return Color.transparent.toString()

    repeat(searchDepth) {
        val color = document.defaultView
            ?.getComputedStyle(currentElement, null)
            ?.getPropertyValue("background-color")

        if (color == null || color == "rgba(0, 0, 0, 0)") {
            currentElement = currentElement.parentElement ?: return@repeat
        } else {
            return color
        }
    }

    return Color.transparent.toString()
}