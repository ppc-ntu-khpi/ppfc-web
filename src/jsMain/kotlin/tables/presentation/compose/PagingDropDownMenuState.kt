/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import coreui.util.ScrollState

data class PagingDropDownMenuState<T : Any>(
    val scrollState: ScrollState = ScrollState.TOP,
    val isLoading: Boolean = false,
    val isExpanded: Boolean = false,
    val selectedItem: T? = null,
    val searchQuery: String = "",
    val error: String? = null
) {
    companion object {
        @Suppress("FunctionName")
        fun <T : Any> Empty() = PagingDropDownMenuState<T>()
    }
}