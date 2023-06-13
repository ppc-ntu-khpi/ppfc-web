/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Box
import coreui.compose.base.Spacer
import coreui.theme.AppStyleSheet.style
import coreui.theme.AppSvgIcon
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import infrastructure.extensions.dateFromString
import infrastructure.extensions.toISO8601String
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.DateInput
import org.w3c.dom.HTMLDivElement
import kotlin.js.Date

private val datePickerFieldWidth = 180.px
private val datePickerFieldHeight = 45.px

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun DatePicker(
    date: Date,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    onDateChange: (date: Date) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        attrs = {
            style {
                width(datePickerFieldWidth)
                maxWidth(datePickerFieldWidth)
                height(datePickerFieldHeight)
                maxHeight(datePickerFieldHeight)
                borderRadius(Shape.small)
                outline("0")
                border {
                    style = LineStyle.Solid
                    width = 2.px
                    color = if (isFocused) {
                        AppTheme.colors.primary
                    } else {
                        AppTheme.colors.outline
                    }
                }
                overflow(Overflow.Hidden)
            }

            applyAttrs(attrs)
        }
    ) {
        Spacer(width = 16.px)

        DateInput(
            attrs = {
                style {
                    width(100.percent)
                    height(100.percent)
                    backgroundColor(Color.transparent)
                    fontSize(Typography.bodyLarge)
                    color(AppTheme.colors.onSurfaceVariant)
                    outline("0")
                    border(width = 0.px)
                }

                "::-webkit-calendar-picker-indicator" style {
                    backgroundImage(AppSvgIcon.Calendar(color = AppTheme.colors.onSurfaceVariant).value)
                    transform {
                        scale(1.3)
                    }
                }

                onFocusIn {
                    isFocused = true
                }

                onFocusOut {
                    isFocused = false
                }

                onChange { listener ->
                    listener.target.blur()
                    onDateChange(dateFromString(listener.value) ?: Date())
                }
            },
            value = date.toISO8601String()
        )

        Spacer(width = 16.px)
    }
}