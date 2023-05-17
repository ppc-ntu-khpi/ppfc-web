/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.*
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TextInput
import org.w3c.dom.HTMLDivElement

private val outlinedTextFieldWidth = 240.px
private val outlinedTextFieldHeight = 45.px

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun OutlinedTextField(
    value: String,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    error: String? = null,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val isLabelElevated = isFocused || value.isNotEmpty()

    Column(
        attrs = {
            style {
                width(outlinedTextFieldWidth)
                backgroundColor(Color.transparent)
            }

            applyAttrs(attrs)
        },
        verticalArrangement = Arrangement.Vertical.Center
    ) {
        Box(
            attrs = {
                style {
                    width(100.percent)
                    height(outlinedTextFieldHeight)
                    marginTop(8.px)
                    backgroundColor(Color.transparent)
                }
            },
            contentAlignment = Alignment.Box.CenterStart
        ) {
            TextInput(
                value = value
            ) {
                style {
                    width(100.percent)
                    height(100.percent)
                    padding(16.px)
                    borderRadius(Shape.small)
                    border {
                        style = LineStyle.Solid
                        width = 2.px
                        color = if (error != null) {
                            AppTheme.colors.error
                        } else if (isFocused) {
                            AppTheme.colors.primary
                        } else {
                            AppTheme.colors.outline
                        }
                    }
                    outline("0")
                    boxSizing("border-box")
                    fontSize(Typography.bodyLarge)
                    backgroundColor(Color.transparent)
                    color(AppTheme.colors.onSurface)
                    transitions {
                        all {
                            duration = 0.15.s
                            timingFunction = AnimationTimingFunction.EaseOut
                        }
                    }
                }

                onFocusIn {
                    isFocused = true
                }

                onFocusOut {
                    isFocused = false
                }

                onInput {
                    onValueChange(it.value)
                }
            }

            Text(
                attrs = {
                    style {
                        marginLeft(14.px)
                        paddingLeft(3.px)
                        paddingRight(3.px)
                        borderRadius(Shape.medium)
                        position(Position.Absolute)
                        pointerEvents(PointerEvents.None)
                        transitions {
                            all {
                                duration = 0.15.s
                                timingFunction = AnimationTimingFunction.EaseOut
                            }
                        }

                        if (error != null) {
                            color(AppTheme.colors.error)
                        } else if (isFocused) {
                            color(AppTheme.colors.primary)
                        } else {
                            color(AppTheme.colors.outline)
                        }

                        if (isLabelElevated) {
                            transform {
                                translate(0.percent, -(outlinedTextFieldHeight / 2.0f))
                            }
                            fontSize(Typography.bodyMedium)
                        } else {
                            backgroundColor(Color.transparent)
                            color(AppTheme.colors.onSurfaceVariant)
                            fontSize(Typography.bodyLarge)
                        }
                    }
                },
                text = label
            )
        }
    }

    if (error != null) {
        Spacer(height = 5.px)

        Text(
            attrs = {
                style {
                    paddingLeft(16.px)
                    paddingRight(16.px)
                    overflowWrap(OverflowWrap.BreakWord)
                    fontSize(Typography.bodyMedium)
                    color(AppTheme.colors.error)
                }
            },
            text = error
        )
    }
}