/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.extensions.elementContext
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import coreui.util.ScrollState
import coreui.util.alpha
import coreui.util.getScrollState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun <K> Menu(
    values: Map<K, String>,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    appendLoaderVisible: Boolean = false,
    onScrollStateChange: (ScrollState) -> Unit,
    onItemSelected: (key: K) -> Unit
) {
    Surface(
        attrs = {
            elementContext { element ->
                element.addEventListener(
                    type = "scroll",
                    callback = {
                        onScrollStateChange(element.getScrollState())
                    }
                )
            }

            style {
                width(100.percent)
                maxWidth(280.px)
                maxHeight(200.px)
                top(100.percent)
                left(0.px)
                right(0.px)
                position(Position.Absolute)
                borderRadius(Shape.small)
                overflowY(Overflow.Auto)
            }

            applyAttrs(attrs)
        },
        shadowElevation = ShadowElevation.Level3,
        tonalElevation = TonalElevation.Level3
    ) {
        Column(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Spacer(height = 8.px)

            values.forEach { item ->
                MenuItem(
                    onClick = {
                        onItemSelected(item.key)
                    }
                ) {
                    Text(
                        text = item.value,
                        fontSize = Typography.bodyLarge,
                        textOverflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(height = 8.px)

            if (appendLoaderVisible) {
                CircularProgressIndicator(
                    size = 40.px
                )

                Spacer(height = 8.px)
            }
        }
    }
}

@Composable
private fun MenuItem(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    Row(
        attrs = {
            style {
                width(100.percent)
                height(48.px)
                paddingLeft(12.px)
                paddingRight(12.px)

                backgroundColor(
                    when {
                        isPressed -> AppTheme.colors.primary.alpha(0.1f)
                        isHovered -> AppTheme.colors.primary.alpha(0.05f)
                        else -> Color.transparent
                    }
                )
            }

            onMouseDown {
                isPressed = true
            }

            onMouseUp {
                isPressed = false
            }

            onMouseOver {
                isHovered = true
            }

            onMouseOut {
                isHovered = false
                isPressed = false
            }

            onClick {
                onClick()
            }

            applyAttrs(attrs)
        },
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        content()
    }
}