/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import coreui.util.alpha
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLButtonElement

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun Button(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isHover by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    org.jetbrains.compose.web.dom.Button(
        attrs = {
            style {
                height(40.px)
                paddingLeft(24.px)
                paddingRight(24.px)
                color(AppTheme.colors.onPrimary)
                backgroundColor(
                    when {
                        isPressed && enabled -> AppTheme.colors.primary.alpha(0.9f)
                        isHover && enabled -> AppTheme.colors.primary.alpha(0.95f)
                        else -> AppTheme.colors.primary
                    }
                )
                border {
                    style = LineStyle.Solid
                    width = 0.px
                    color = Color.transparent
                }
                borderRadius(Shape.extraLarge)
                textDecorationLine("none")
                textAlign(TextAlign.Center)
                fontWeight(FontWeight.Bold)
                fontSize(Typography.titleMedium)
                display(DisplayStyle.InlineBlock)
                cursor("pointer")
                opacity(if (enabled) 100.percent else 36.percent)
                pointerEvents(if (enabled) PointerEvents.Auto else PointerEvents.None)
                boxShadow(ShadowElevation.Level2)
                transitions {
                    defaultDuration(0.15.s)
                }
            }

            onMouseDown {
                isPressed = true
            }

            onMouseUp {
                isPressed = false
            }

            onMouseOver {
                isHover = true
            }

            onMouseOut {
                isHover = false
                isPressed = false
            }

            onClick {
                if (enabled) {
                    onClick()
                }
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}