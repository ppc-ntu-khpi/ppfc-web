/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import androidx.compose.runtime.*
import app.cash.paging.LoadStateLoading
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.theme.AppIconClass
import coreui.util.LazyPagingItems
import coreui.util.ScrollState
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun <T : Any> PagingDropDownMenu(
    state: PagingDropDownMenuState<T>,
    lazyPagingItems: LazyPagingItems<T>,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemLabel: (item: T) -> String,
    onStateChanged: (state: PagingDropDownMenuState<T>) -> Unit
) {
    val itemsNumber = lazyPagingItems.itemCount
    val isAppending = lazyPagingItems.loadState.append == LoadStateLoading
    var listScrollState by remember { mutableStateOf(ScrollState.TOP) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(listScrollState == ScrollState.BOTTOM) {
        try {
            lazyPagingItems[(itemsNumber - 1).coerceAtLeast(0)]
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    LaunchedEffect(isExpanded) {
        delay(1000)

        onStateChanged(
            state.copy(
                isExpanded = isExpanded
            )
        )
    }

    Column(
        attrs = {
            style {
                position(Position.Relative)
                display(DisplayStyle.InlineBlock)
                overflowY(Overflow.Visible)
                zIndex(1)
            }

            onFocusIn {
                isExpanded = true
            }

            onFocusOut {
                isExpanded = false
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            OutlinedTextField(
                value = state.selectedItem?.let { item ->
                    itemLabel(item)
                } ?: state.searchQuery,
                label = label,
                attrs = attrs,
                error = state.error,
                trailingIcon = AppIconClass.Cancel,
                onTrailingIconClick = {
                    onStateChanged(
                        state.copy(
                            selectedItem = null,
                            searchQuery = "",
                            isExpanded = false
                        )
                    )
                },
                onValueChange = { text ->
                    onStateChanged(
                        state.copy(
                            selectedItem = null,
                            searchQuery = text
                        )
                    )
                }
            )
        }

        if (!state.isExpanded) return@Column

        Menu(
            values = run {
                val values = mutableMapOf<T, String>()
                (0 until itemsNumber).forEach { index ->
                    val item = lazyPagingItems.peek(index) ?: return@forEach
                    values += item to itemLabel(item)
                }
                values
            },
            appendLoaderVisible = isAppending,
            onScrollStateChange = { scrollState ->
                listScrollState = scrollState
            }
        ) { item ->
            onStateChanged(
                state.copy(
                    selectedItem = item,
                    isExpanded = false
                )
            )
        }
    }
}