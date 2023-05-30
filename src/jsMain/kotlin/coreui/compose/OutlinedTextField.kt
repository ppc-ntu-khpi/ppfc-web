/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.*
import coreui.extensions.elementContext
import coreui.extensions.getActualBackgroundColor
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.EmailInput
import org.jetbrains.compose.web.dom.PasswordInput
import org.jetbrains.compose.web.dom.TextInput
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement

private val outlinedTextFieldWidth = 240.px
private val outlinedTextFieldHeight = 45.px

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun OutlinedTextField(
    value: String,
    label: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    textFieldType: TextFieldType = TextFieldType.TEXT,
    trailingIcon: AppIconClass? = null,
    onTrailingIconClick: () -> Unit = {},
    error: String? = null,
    onValueChange: (text: String) -> Unit
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
                    backgroundColor(Color.transparent)
                }
            },
            contentAlignment = Alignment.Box.CenterStart
        ) {
            Box(
                attrs = {
                    style {
                        width(100.percent)
                        height(100.percent)
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
                        transitions {
                            all {
                                duration = 0.15.s
                                timingFunction = AnimationTimingFunction.EaseOut
                            }
                        }
                    }
                },
                contentAlignment = Alignment.Box.CenterStart
            ) textFieldBox@ {
                Spacer(width = 16.px)

                val inputContent: InputAttrsScope<String>.() -> Unit = {
                    style {
                        width(100.percent)
                        height(100.percent)
                        backgroundColor(Color.transparent)
                        outline("0")
                        border(width = 0.px)
                        boxSizing("border-box")
                        fontSize(Typography.bodyLarge)
                        color(AppTheme.colors.onSurface)
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

                when (textFieldType) {
                    TextFieldType.TEXT -> {
                        TextInput(
                            attrs = inputContent,
                            value = value
                        )
                    }

                    TextFieldType.PASSWORD -> {
                        PasswordInput(
                            attrs = inputContent,
                            value = value
                        )
                    }

                    TextFieldType.EMAIL -> {
                        EmailInput(
                            attrs = inputContent,
                            value = value
                        )
                    }
                }

                Spacer(width = 16.px)

                if (trailingIcon == null) return@textFieldBox

                Icon(
                    attrs = {
                        onClick {
                            onTrailingIconClick()
                        }
                    },
                    size = 20.px,
                    icon = trailingIcon,
                    tint = AppTheme.colors.onSurfaceVariant
                )

                Spacer(width = 16.px)
            }

            var labelElement by remember { mutableStateOf<Element?>(null) }
            var labelBackgroundColor by remember { mutableStateOf(Color.transparent.toString()) }
            LaunchedEffect(AppTheme.colors) {
                labelBackgroundColor = labelElement?.getActualBackgroundColor() ?: return@LaunchedEffect
            }

            Text(
                attrs = {
                    elementContext { element ->
                        labelElement = element
                    }

                    style {
                        marginLeft(14.px)
                        paddingLeft(3.px)
                        paddingRight(3.px)
                        position(Position.Absolute)
                        pointerEvents(PointerEvents.None)
                        transitions {
                            "transform" {
                                duration = 0.15.s
                                timingFunction = AnimationTimingFunction.EaseOut
                            }

                            "font-size" {
                                duration = 0.15.s
                                timingFunction = AnimationTimingFunction.EaseOut
                            }
                        }
                        borderRadius(Shape.extraSmall)

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
                            color(AppTheme.colors.onSurfaceVariant)
                            fontSize(Typography.bodyLarge)
                        }

                        backgroundColor(labelBackgroundColor)
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