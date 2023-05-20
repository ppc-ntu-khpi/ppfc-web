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
import org.jetbrains.compose.web.css.*

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckChange: (isChecked: Boolean) -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    Box(
        attrs = {
            style {
                width(16.px)
                height(16.px)
                maxWidth(16.px)
                maxHeight(16.px)
                borderRadius(Shape.extraSmall)

                if(checked) {
                    backgroundColor(
                        when {
                            isPressed -> AppTheme.colors.primary.alpha(0.9f)
                            isHovered -> AppTheme.colors.primary.alpha(0.95f)
                            else -> AppTheme.colors.primary
                        }
                    )
                    border {
                        width = 2.px
                        color = Color.transparent
                        style = LineStyle.Solid
                    }
                } else {
                    backgroundColor(
                        when {
                            isPressed -> AppTheme.colors.primary.alpha(0.1f)
                            isHovered -> AppTheme.colors.primary.alpha(0.05f)
                            else -> Color.transparent
                        }
                    )
                    border {
                        width = 2.px
                        color = AppTheme.colors.outline
                        style = LineStyle.Solid
                    }
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
                onCheckChange(!checked)
            }
        },
        contentAlignment = Alignment.Box.Center
    ) {
        if (!checked) return@Box

        Icon(
            size = 14.px,
            icon = AppIconClass.Check,
            tint = AppTheme.colors.onPrimary
        )
    }
}