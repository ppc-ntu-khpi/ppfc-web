/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
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
    state: DropDownMenuState<T>,
    items: List<T>,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    itemLabel: (item: T) -> String,
    onStateChanged: (state: DropDownMenuState<T>) -> Unit
) {
    var localState by remember { mutableStateOf(state) }

    LaunchedEffect(localState) {
        onStateChanged(localState)
    }

    Column(
        attrs = {
            style {
                position(Position.Relative)
                display(DisplayStyle.InlineBlock)
                overflowY(Overflow.Visible)
                zIndex(1)
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            OutlinedTextField(
                attrs = {
                    elementContext { element ->
                        document.addEventListener(
                            type = "click",
                            callback = { event ->
                                val clickedOutside = !element.contains(event.target.asDynamic() as? Node)
                                if (clickedOutside) {
                                    localState = localState.copy(
                                        isExpanded = false
                                    )
                                }
                            }
                        )
                    }

                    onFocusIn {
                        localState = localState.copy(
                            isExpanded = true
                        )
                    }

                    applyAttrs(attrs)
                },
                value = itemLabel(state.selectedItem),
                label = label,
                onValueChange = {}
            )
        }

        if (!localState.isExpanded) return@Column

        Menu(
            values = items.associateWith { item -> itemLabel(item) }
        ) { item ->
            localState = localState.copy(
                selectedItem = item,
                isExpanded = false
            )
        }
    }
}