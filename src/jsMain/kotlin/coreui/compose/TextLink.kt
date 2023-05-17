/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import coreui.theme.AppTheme
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun TextLink(
    text: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    selectable: Boolean = false,
    color: CSSColorValue? = null,
    fontSize: CSSNumeric? = null,
    fontWeight: FontWeight? = null,
    fontFamily: String? = null,
    textAlign: TextAlign? = null,
    lineHeight: CSSNumeric? = null,
    textOverflow: TextOverflow? = null,
    overflowWrap: OverflowWrap? = null,
    onClick: () -> Unit
) {
    var isHover by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    Text(
        attrs = {
            style {
                color(AppTheme.colors.primary)
                filter {
                    val brightness = when {
                        isPressed -> 80.percent
                        isHover -> 90.percent
                        else -> 100.percent
                    }
                    brightness(brightness)
                }
                cursor(Cursor.Pointer)
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
                onClick()
            }

            applyAttrs(attrs)
        },
        text = text,
        selectable = selectable,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        textAlign = textAlign,
        lineHeight = lineHeight,
        textOverflow = textOverflow,
        overflowWrap = overflowWrap
    )
}