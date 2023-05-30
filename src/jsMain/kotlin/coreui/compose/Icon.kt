/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.I
import org.w3c.dom.HTMLElement

@Composable
fun Icon(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    size: CSSNumeric,
    icon: AppIconClass,
    tint: CSSColorValue = AppTheme.colors.primary
) {
    I(
        attrs = {
            attr("class", icon.value)

            style {
                fontSize(size)
                color(tint)
            }

            applyAttrs(attrs)
        }
    )
}