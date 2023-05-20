/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.util.alpha
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLButtonElement

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun IconButton(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    enabled: Boolean = true,
    tint: CSSColorValue = AppTheme.colors.onPrimary,
    icon: AppIconClass,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    org.jetbrains.compose.web.dom.Button(
        attrs = {
            style {
                width(40.px)
                height(40.px)
                backgroundColor(
                    when {
                        isPressed && enabled -> AppTheme.colors.primary.alpha(0.9f)
                        isHovered && enabled -> AppTheme.colors.primary.alpha(0.95f)
                        else -> AppTheme.colors.primary
                    }
                )
                border {
                    style = LineStyle.Solid
                    width = 0.px
                    color = Color.transparent
                }
                borderRadius(Shape.round)
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
                isHovered = true
            }

            onMouseOut {
                isHovered = false
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
        Box(
            attrs = {
                style {
                    width(100.percent)
                    height(100.percent)
                }
            },
            contentAlignment = Alignment.Box.Center
        ) {
            Icon(
                size = 20.px,
                icon = icon,
                tint = tint
            )
        }
    }
}