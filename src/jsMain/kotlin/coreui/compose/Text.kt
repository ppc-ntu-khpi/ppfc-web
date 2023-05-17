/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.compose.base.Box
import coreui.theme.AppStyleSheet
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement

@Composable
fun Text(
    text: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    selectable: Boolean = false,
    color: CSSColorValue? = null,
    fontSize: CSSNumeric? = null,
    fontWeight: FontWeight? = null,
    fontFamily: String? = null,
    textAlign: TextAlign? = null,
    lineHeight: CSSNumeric? = null,
    textOverflow: TextOverflow? = null,
    overflowWrap: OverflowWrap? = null
) {
    Box(
        attrs = {
            if(!selectable) {
                classes(AppStyleSheet.notSelectable)
            }

            style {
                whiteSpace("pre-wrap")
                color?.let { color(it) }
                fontSize?.let { fontSize(it) }
                fontWeight?.let { fontWeight(it) }
                fontFamily?.let { fontFamily(it) }
                textAlign?.let { textAlign(it) }
                lineHeight?.let { lineHeight(it) }
                textOverflow?.let { textOverflow(it) }
                overflowWrap?.let { overflowWrap(it) }
            }

            applyAttrs(attrs)
        }
    ) {
        Text(text)
    }
}