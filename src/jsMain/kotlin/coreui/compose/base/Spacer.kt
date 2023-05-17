/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose.base

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun Spacer(
    width: CSSNumeric = 0.px,
    height: CSSNumeric = 0.px
) {
    Div(
        attrs = {
            style {
                width(width)
                height(height)
            }
        }
    )
}