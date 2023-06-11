/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.extensions

import core.extensions.onEachWithPrevious
import kotlinx.coroutines.flow.*
import tables.presentation.compose.PagingDropDownMenuState

fun <T : Any> Flow<PagingDropDownMenuState<T>>.onSearchQuery(
    action: suspend (searchQuery: String) -> Unit
): Flow<String> = this.distinctUntilChangedBy {
    it.searchQuery
}.map { it.searchQuery }.onEach { searchQuery ->
    action(searchQuery)
}

fun <T : Any> Flow<List<PagingDropDownMenuState<T>>>.onSearchQuery(
    action: suspend (searchQuery: String) -> Unit
): Flow<String> = this.distinctUntilChangedBy { state ->
    state.map { it.searchQuery }
}.onEachWithPrevious { old, new ->
    new.map { it.searchQuery }.toSet().subtract(old.map { it.searchQuery }.toSet()).firstOrNull() ?: ""
}.onEach { searchQuery ->
    action(searchQuery)
}

fun <T : Any> Flow<List<PagingDropDownMenuState<T>>>.onItem(
    action: suspend (selectedItem: T?) -> Unit
): Flow<T?> = this.distinctUntilChangedBy { state ->
    state.map { it.selectedItem }
}.onEachWithPrevious { old, new ->
    new.map { it.selectedItem }.toSet().subtract(old.map { it.selectedItem }.toSet()).firstOrNull()
}.distinctUntilChanged().onEach { selectedItem ->
    action(selectedItem)
}