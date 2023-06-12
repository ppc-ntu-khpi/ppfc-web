/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import androidx.compose.runtime.*
import app.cash.paging.LoadStateLoading
import coreui.compose.*
import coreui.compose.base.Column
import coreui.extensions.elementContext
import coreui.theme.AppIconClass
import coreui.util.LazyPagingItems
import coreui.util.ScrollState
import coreui.util.getScrollState
import kotlinx.browser.document
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node

@Composable
fun <T : Any> PagingDropDownMenu(
    state: PagingDropDownMenuState<T>,
    lazyPagingItems: LazyPagingItems<T>,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemLabel: (item: T) -> String,
    onStateChanged: (state: PagingDropDownMenuState<T>) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var menuElement by remember { mutableStateOf<Element?>(null) }
    val itemsNumber = lazyPagingItems.itemCount
    val isRefreshing = lazyPagingItems.loadState.refresh == LoadStateLoading
    val isAppending = lazyPagingItems.loadState.append == LoadStateLoading
    val isLoading = isRefreshing || isAppending

    Column(
        attrs = {
            style {
                position(Position.Relative)
                display(DisplayStyle.InlineBlock)
                overflowY(Overflow.Visible)
            }

            elementContext { element ->
                document.addEventListener(
                    type = "click",
                    callback = { event ->
                        val clickedOutside = !element.contains(event.target.asDynamic() as? Node)
                        if (clickedOutside) {
                            isExpanded = false
                        }
                    }
                )
            }
        }
    ) {
        OutlinedTextField(
            attrs = {
                onFocusIn {
                    isExpanded = true
                }

                applyAttrs(attrs)
            },
            value = state.selectedItem?.let { item ->
                itemLabel(item)
            } ?: state.searchQuery,
            label = label,
            error = state.error,
            trailingIcon = if (state.selectedItem != null) {
                AppIconClass.Cancel
            } else null,
            onTrailingIconClick = {
                isExpanded = false
                onStateChanged(
                    state.copy(
                        selectedItem = null,
                        searchQuery = ""
                    )
                )
            },
            onValueChange = { text ->
                menuElement?.scroll(x = 0.0, y = 0.0)

                onStateChanged(
                    state.copy(
                        selectedItem = null,
                        searchQuery = text
                    )
                )
            }
        )

        if (!isExpanded) return@Column

        Menu(
            attrs = {
                elementContext {
                    menuElement = it
                }

                onScroll {
                    val scrollState = menuElement?.getScrollState() ?: return@onScroll
                    if (scrollState != ScrollState.BOTTOM) return@onScroll

                    try {
                        lazyPagingItems[(itemsNumber - 1).coerceAtLeast(0)]
                    } catch (_: IndexOutOfBoundsException) {
                    }
                }
            },
            isLoading = isLoading,
            values = lazyPagingItems.itemSnapshotList.items.associateWith { item -> itemLabel(item) }
        ) { item ->
            isExpanded = false
            onStateChanged(
                state.copy(
                    selectedItem = item
                )
            )
        }
    }
}