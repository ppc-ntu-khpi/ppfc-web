/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

import androidx.compose.runtime.*
import app.cash.paging.LoadStateLoading
import coreui.compose.*
import coreui.compose.base.*
import coreui.theme.AppIconClass
import coreui.theme.AppStyleSheet.style
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.util.LazyPagingItems
import coreui.util.alpha
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Tr
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement
import kotlin.math.min

@Composable
fun <T : Any> PagingTable(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemsPerPage: Long = 10,
    lazyPagingItems: LazyPagingItems<T>,
    header: @Composable PagingTableHeaderScope.() -> Unit,
    body: @Composable PagingTableBodyScope.(index: Long, item: T) -> Unit
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

                            "tbody tr:nth-of-type(odd)" style {
                                backgroundColor(AppTheme.colors.primary.alpha(0.1f))
                            }
                        }
                    },
                    header = {
                        PagingTableHeaderScopeImpl().header()
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

                            PagingTableBodyScopeImpl().body(index, item)
                        }
                    }
                )
            }
        }
    }
}

interface PagingTableHeaderScope
class PagingTableHeaderScopeImpl : PagingTableHeaderScope

@Composable
fun PagingTableHeaderScope.row(
    attrs: AttrBuilderContext<HTMLTableRowElement>? = null,
    content: @Composable () -> Unit
) {
    TableRow(
        attrs = {
            style {
                position(Position.Sticky)
                top(0.px)
                zIndex(10)
            }

            applyAttrs(attrs)
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

        content()

        TableHeaderItem(
            attrs = {
                style {
                    width(100.percent)
                    textAlign(TextAlign.End)
                }
            }
        ) { }
    }
}

@Composable
fun PagingTableHeaderScope.item(
    content: @Composable () -> Unit
) {
    TableHeaderItem {
        content()
    }
}

interface PagingTableBodyScope
class PagingTableBodyScopeImpl : PagingTableBodyScope

@Composable
fun PagingTableBodyScope.row(
    attrs: AttrBuilderContext<HTMLTableRowElement>? = null,
    isSelected: Boolean,
    onSelectionChanged: ((isSelected: Boolean) -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Tr(
        attrs = {
            style {
                width(100.percent)
                height(64.px)
                minHeight(64.px)
            }

            applyAttrs(attrs)
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
            if (onSelectionChanged == null) return@TableBodyItem

            Checkbox(isSelected) {
                onSelectionChanged(it)
            }
        }

        content()

        TableBodyItem(
            attrs = {
                style {
                    width(100.percent)
                    textAlign(TextAlign.End)
                }
            }
        ) {
            if (onEdit == null) return@TableBodyItem

            IconButton(
                icon = AppIconClass.Edit
            ) {
                onEdit()
            }
        }
    }
}

@Composable
fun PagingTableBodyScope.item(
    content: @Composable () -> Unit
) {
    TableBodyItem {
        content()
    }
}
