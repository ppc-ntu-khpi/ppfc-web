/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.model

data class NumberFieldState(
    val number: Long? = null,
    val enabled: Boolean = true,
    val error: String? = null
) {
    companion object {
        val Empty = NumberFieldState()
    }
}
