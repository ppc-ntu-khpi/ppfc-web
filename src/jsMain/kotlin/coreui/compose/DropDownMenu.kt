/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Column
import coreui.extensions.elementContext
import kotlinx.browser.document
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node

@Composable
fun <T : Any> DropDownMenu(
    items: List<T>,
    selectedItem: T,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemLabel: (item: T) -> String,
    onItemSelected: (item: T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        attrs = {
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

            style {
                position(Position.Relative)
                display(DisplayStyle.InlineBlock)
                overflowY(Overflow.Visible)
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
            value = itemLabel(selectedItem),
            label = label,
            onValueChange = {}
        )

        if (!isExpanded) return@Column

        Menu(
            values = items.associateWith { item -> itemLabel(item) }
        ) { item ->
            isExpanded = false
            onItemSelected(item)
        }
    }
}