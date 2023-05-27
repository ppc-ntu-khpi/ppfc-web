/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun OutlinedNumberField(
    value: Long?,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    symmetricLayout: Boolean = false,
    error: String? = null,
    onValueChange: (number: Long?) -> Unit
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        label = label,
        attrs = attrs,
        symmetricLayout = symmetricLayout,
        error = error,
        onValueChange = { text ->
            for(char in text) {
                if(!char.isDigit()) return@OutlinedTextField
            }
            onValueChange(text.toLongOrNull())
        }
    )
}