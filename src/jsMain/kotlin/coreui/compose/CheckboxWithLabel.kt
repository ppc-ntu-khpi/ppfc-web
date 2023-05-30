/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun CheckboxWithLabel(
    checked: Boolean,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    onCheckChange: (isChecked: Boolean) -> Unit
) {
    Row(
        attrs = {
            applyAttrs(attrs)
        },
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalArrangement = Arrangement.Horizontal.Start
    ) {
        Checkbox(checked) {
            onCheckChange(!checked)
        }

        Spacer(width = 5.px)

        Text(
            attrs = {
                onClick {
                    onCheckChange(!checked)
                }
            },
            text = label
        )
    }
}