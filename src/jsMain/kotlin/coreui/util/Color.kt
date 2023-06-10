/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgba

fun color(value: Long): CSSColorValue {
    val red = (value shr 16 and 0xFF)
    val green = (value shr 8 and 0xFF)
    val blue = (value and 0xFF)
    val alpha = (value shr 24 and 0xFF).toFloat() / 255.0f
    return rgba(red, green, blue, alpha)
}

fun CSSColorValue.alpha(alpha: Float): CSSColorValue {
    return org.jetbrains.compose.web.css.Color(this.toString().removeSuffix("1)") + "$alpha)")
}