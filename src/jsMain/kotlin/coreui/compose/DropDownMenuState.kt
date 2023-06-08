/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

data class DropDownMenuState<T : Any>(
    val isExpanded: Boolean = false,
    val selectedItem: T
) {
    companion object {
        @Suppress("FunctionName")
        fun <T : Any> Empty(selectedItem: T) = DropDownMenuState(selectedItem = selectedItem)
    }
}