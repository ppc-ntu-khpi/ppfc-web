/*
 * Copyright (c) 2023. Vladyslava Vasylieva
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
