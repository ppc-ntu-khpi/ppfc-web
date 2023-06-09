/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

data class PagingDropDownMenuState<T : Any>(
    val selectedItem: T? = null,
    val searchQuery: String = "",
    val error: String? = null
) {
    companion object {
        @Suppress("FunctionName")
        fun <T : Any> Empty() = PagingDropDownMenuState<T>()
    }
}