/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.theme.AppTheme
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.transform
import org.jetbrains.compose.web.dom.CheckboxInput

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    CheckboxInput(
        checked = checked
    ) {
        style {
            accentColor(AppTheme.colors.primary)
            transform {
                scale(1.5)
                translateY((15 / 2).percent)
            }
        }

        onInput {
            onCheckChange(it.value)
        }
    }
}