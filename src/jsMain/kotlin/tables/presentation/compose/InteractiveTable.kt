package tables.presentation.compose

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.theme.AppIconClass
import coreui.theme.Shape
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Tr
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement
import kotlin.random.Random

@Composable
fun InteractiveTable(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    isRefreshing: Boolean = false,
    isAppending: Boolean = false,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    var isScrolledToBottom by remember { mutableStateOf(false) }

    Surface(
        attrs = {
            style {
                borderRadius(Shape.extraLarge)
                overflow(Overflow.Clip)
            }

            applyAttrs(attrs)
        },
        shadowElevation = ShadowElevation.Level3,
        tonalElevation = TonalElevation.Level3
    ) {
        Column(
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Table(
                attrs = {
                    val id = Random.Default.nextLong().toString()
                    id(id)

                    style {
                        width(100.percent)
                    }

                    onScroll {
                        val thisElement = document.getElementById(id) ?: return@onScroll
                        val scrollHeight = thisElement.scrollHeight
                        val scrollTop = thisElement.scrollTop
                        val clientHeight = thisElement.clientHeight

                        isScrolledToBottom = (scrollHeight - scrollTop - clientHeight) < 0
                        println(isScrolledToBottom)
                    }
                },
                header = {
                    header()
                },
                body = {
                    body()
                }
            )

            if (isAppending || isRefreshing) {
                CircularProgressIndicator(
                    attrs = {
                        style {
                            width(40.px)
                            height(40.px)
                            margin(5.px)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun InteractiveTableHeaderRow(
    attrs: AttrBuilderContext<HTMLTableRowElement>? = null,
    content: @Composable () -> Unit
) {
    TableRow(
        attrs = {
            style {
                position(Position.Sticky)
                top(0.px)
                property("z-index", 10)
            }

            applyAttrs(attrs)
        }
    ) {
        TableHeaderItem(
            attrs = {
                style {
                    maxWidth(30.px)
                    width(30.px)
                    textAlign(TextAlign.Center)
                }
            }
        ) { }

        content()

        TableHeaderItem(
            attrs = {
                style {
                    maxWidth(30.px)
                    width(30.px)
                    textAlign(TextAlign.Center)
                }
            }
        ) { }
    }
}

@Composable
fun InteractiveTableBodyRow(
    attrs: AttrBuilderContext<HTMLTableRowElement>? = null,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    onEdit: () -> Unit,
    content: @Composable () -> Unit
) {
    Tr(
        attrs = {
            applyAttrs(attrs)
        }
    ) {
        TableBodyItem(
            attrs = {
                style {
                    maxWidth(50.px)
                    width(50.px)
                    textAlign(TextAlign.Center)
                }
            }
        ) {
            Checkbox(isSelected) {
                onSelectionChanged(it)
            }
        }

        content()

        TableBodyItem(
            attrs = {
                style {
                    maxWidth(50.px)
                    width(50.px)
                    textAlign(TextAlign.Center)
                }
            }
        ) {
            IconButton(
                icon = AppIconClass.Edit
            ) {
                onEdit()
            }
        }
    }
}