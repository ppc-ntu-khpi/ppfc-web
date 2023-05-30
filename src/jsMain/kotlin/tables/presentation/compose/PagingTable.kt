/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import androidx.compose.runtime.*
import app.cash.paging.LoadStateLoading
import coreui.compose.*
import coreui.compose.base.*
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.util.LazyPagingItems
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Tr
import org.w3c.dom.HTMLDivElement
import kotlin.math.min

@Composable
fun <T : Any> PagingTable(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemsPerPage: Long = 10,
    lazyPagingItems: LazyPagingItems<T>,
    header: TableHeaderRow,
    selectionEnabled: Boolean = true,
    editingEnabled: Boolean = true,
    bodyItem: (item: T) -> TableBodyRow
) {
    val isRefreshing = lazyPagingItems.loadState.refresh == LoadStateLoading

    val itemsNumber = lazyPagingItems.itemCount.toLong()
    var currentPage by remember { mutableStateOf(0L) }
    val lastPage = (itemsNumber - 1).coerceAtLeast(0) / itemsPerPage

    currentPage = min(currentPage, lastPage)

    Surface(
        attrs = {
            style {
                borderRadius(Shape.extraLarge)
                overflow(Overflow.Hidden)
            }

            applyAttrs(attrs)
        },
        shadowElevation = ShadowElevation.Level3,
        tonalElevation = TonalElevation.Level3
    ) {
        Row(
            attrs = {
                style {
                    width(100.percent)
                    height(100.percent)
                }
            }
        ) {
            Column(
                attrs = {
                    style {
                        margin(10.px)
                    }
                },
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                IconButton(
                    enabled = !isRefreshing,
                    icon = AppIconClass.Refresh
                ) {
                    currentPage = 0
                    try {
                        lazyPagingItems[0]
                    } catch (_: IndexOutOfBoundsException) {
                    }
                    lazyPagingItems.refresh()
                }

                Spacer(height = 10.px)

                val canMoveUp = currentPage > 0L && !isRefreshing
                IconButton(
                    enabled = canMoveUp,
                    icon = AppIconClass.ArrowUp
                ) {
                    if (!canMoveUp) return@IconButton
                    currentPage--
                }

                Spacer(height = 10.px)

                Text(text = (currentPage + 1).toString())

                Spacer(height = 10.px)

                val canMoveDown = currentPage < lastPage && !isRefreshing
                IconButton(
                    enabled = canMoveDown,
                    icon = AppIconClass.ArrowDown
                ) {
                    if (!canMoveDown) return@IconButton
                    currentPage++
                }
            }

            Box(
                attrs = {
                    style {
                        width(1.px)
                        height(100.percent)
                        backgroundColor(AppTheme.colors.outline)
                    }
                }
            )

            if (isRefreshing && itemsNumber == 0L) {
                Box(
                    attrs = {
                        style {
                            width(100.percent)
                            height(100.percent)
                        }
                    },
                    contentAlignment = Alignment.Box.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Row
            }

            if (itemsNumber == 0L && !isRefreshing) {
                Column(
                    attrs = {
                        style {
                            width(100.percent)
                            height(100.percent)
                        }
                    },
                    verticalArrangement = Arrangement.Vertical.Center,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Icon(
                        size = 80.px,
                        icon = AppIconClass.EmptyTable,
                        tint = AppTheme.colors.onBackground
                    )

                    Spacer(height = 10.px)

                    Text(text = AppTheme.stringResources.tableRecordsNotFound)
                }
                return@Row
            }

            Column(
                attrs = {
                    style {
                        width(100.percent)
                        height(100.percent)
                        overflow(Overflow.Auto)
                    }
                },
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Table(
                    attrs = {
                        style {
                            width(100.percent)
                        }
                    },
                    header = {
                        TableRow(
                            attrs = {
                                style {
                                    position(Position.Sticky)
                                    top(0.px)
                                    zIndex(10)
                                }
                            }
                        ) {
                            TableHeaderItem(
                                attrs = {
                                    style {
                                        width(1.percent)
                                        textAlign(TextAlign.Start)
                                    }
                                }
                            ) { }

                            header.data.forEach { text ->
                                TableHeaderItem {
                                    Text(
                                        text = text,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }

                            TableHeaderItem(
                                attrs = {
                                    style {
                                        width(100.percent)
                                        textAlign(TextAlign.End)
                                    }
                                }
                            ) { }
                        }
                    },
                    body = {
                        val start = currentPage * itemsPerPage
                        val end = (start + itemsPerPage).coerceAtMost(itemsNumber)

                        for (index in start until end) {
                            val item = try {
                                lazyPagingItems[index.toInt()]
                            } catch (e: IndexOutOfBoundsException) {
                                null
                            } ?: break

                            val bodyRow = bodyItem(item)

                            Tr(
                                attrs = {
                                    style {
                                        width(100.percent)
                                        height(64.px)
                                        minHeight(64.px)
                                    }
                                }
                            ) {
                                TableBodyItem(
                                    attrs = {
                                        style {
                                            width(1.percent)
                                            textAlign(TextAlign.Start)
                                        }
                                    }
                                ) {
                                    if(!selectionEnabled) return@TableBodyItem

                                    Checkbox(bodyRow.isSelected) {
                                        bodyRow.onSelectionChanged(it)
                                    }
                                }

                                bodyRow.data.forEach { text ->
                                    TableBodyItem {
                                        Text(
                                            text = text,
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }

                                TableBodyItem(
                                    attrs = {
                                        style {
                                            width(100.percent)
                                            textAlign(TextAlign.End)
                                        }
                                    }
                                ) {
                                    if(!editingEnabled) return@TableBodyItem

                                    IconButton(
                                        icon = AppIconClass.Edit
                                    ) {
                                        bodyRow.onEdit()
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

data class TableHeaderRow(
    val data: List<String>
)

fun tableHeaderRow(
    vararg data: String
) = TableHeaderRow(
    data = data.toList()
)

data class TableBodyRow(
    val isSelected: Boolean,
    val onSelectionChanged: (isSelected: Boolean) -> Unit,
    val onEdit: () -> Unit,
    val data: List<String>
)

fun tableBodyRow(
    isSelected: Boolean,
    onSelectionChanged: (isSelected: Boolean) -> Unit,
    onEdit: () -> Unit,
    vararg data: String
) = TableBodyRow(
    isSelected = isSelected,
    onSelectionChanged = onSelectionChanged,
    onEdit = onEdit,
    data = data.toList()
)