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
import coreui.util.ScrollState
import coreui.util.elementContext
import coreui.util.getScrollState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Tr
import org.w3c.dom.HTMLDivElement

@Composable
fun <T : Any> PagingTable(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    lazyPagingItems: LazyPagingItems<T>,
    header: TableHeaderRow,
    bodyItem: (item: T) -> TableBodyRow
) {
    val isRefreshing = lazyPagingItems.loadState.refresh == LoadStateLoading
    val isAppending = lazyPagingItems.loadState.append == LoadStateLoading

    var listScrollState by remember { mutableStateOf(ScrollState.TOP) }

    LaunchedEffect(listScrollState == ScrollState.BOTTOM) {
        try {
            lazyPagingItems[lazyPagingItems.itemCount]
        } catch (_: Exception) {
        }
    }

    Surface(
        attrs = {
            elementContext { element ->
                element.addEventListener(
                    type = "scroll",
                    callback = {
                        listScrollState = element.getScrollState(deviation = 0.0)
                        println(listScrollState.name)
                    }
                )
            }

            style {
                borderRadius(Shape.extraLarge)
                overflow(Overflow.Auto)
            }

            applyAttrs(attrs)
        },
        shadowElevation = ShadowElevation.Level3,
        tonalElevation = TonalElevation.Level3
    ) {
        if (isRefreshing) {
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
        } else if (lazyPagingItems.itemCount == 0 && !isRefreshing) {
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
        } else {
            Column(
                attrs = {
                    style {
                        width(100.percent)
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
                                    Text(text = text)
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
                        (0 until lazyPagingItems.itemCount).forEach { index ->
                            val item = lazyPagingItems.peek(index) ?: return@forEach
                            val bodyRow = bodyItem(item)

                            Tr {
                                TableBodyItem(
                                    attrs = {
                                        style {
                                            width(1.percent)
                                            textAlign(TextAlign.Start)
                                        }
                                    }
                                ) {
                                    Checkbox(bodyRow.isSelected) {
                                        bodyRow.onSelectionChanged(it)
                                    }
                                }

                                bodyRow.data.forEach { text ->
                                    TableBodyItem {
                                        Text(text = text)
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

                if (isAppending) {
                    CircularProgressIndicator(
                        attrs = {
                            style {
                                marginTop(10.px)
                                marginBottom(10.px)
                            }
                        }
                    )
                }
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