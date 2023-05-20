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
fun OutlinedButton(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    org.jetbrains.compose.web.dom.Button(
        attrs = {
            style {
                height(40.px)
                paddingLeft(24.px)
                paddingRight(24.px)
                backgroundColor(
                    when {
                        isPressed && enabled -> AppTheme.colors.primary.alpha(0.1f)
                        isHovered && enabled -> AppTheme.colors.primary.alpha(0.05f)
                        else -> Color.transparent
                    }
                )
                color(AppTheme.colors.primary)
                borderRadius(Shape.extraLarge)
                borderWidth(2.px)
                border {
                    style = LineStyle.Solid
                    width = 2.px
                    color = AppTheme.colors.primary
                }
                textDecorationLine("none")
                textAlign(TextAlign.Center)
                fontWeight(FontWeight.Bold)
                fontSize(Typography.titleMedium)
                display(DisplayStyle.InlineBlock)
                cursor("pointer")
                opacity(if (enabled) 100.percent else 36.percent)
                pointerEvents(if (enabled) PointerEvents.Auto else PointerEvents.None)
                boxShadow(ShadowElevation.Level2)
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
        }
    ) {
        content()
    }
}