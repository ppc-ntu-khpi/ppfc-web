/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.model

data class TextFieldState(
    val text: String = "",
    val enabled: Boolean = true,
    val error: String? = null
) {
    companion object {
        val Empty = TextFieldState()
    }
}
