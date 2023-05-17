/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import org.jetbrains.compose.web.css.*

sealed class ShadowElevation(
    val offsetX: CSSLengthValue,
    val offsetY: CSSLengthValue,
    val blurRadius: CSSLengthValue,
    val color: CSSColorValue
) {
    object Level0 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 0.px,
        blurRadius = 0.px,
        color = Color.transparent
    )

    object Level1 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 2.px,
        blurRadius = 2.px,
        color = rgba(0, 0, 0, 0.24)
    )

    object Level2 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 3.px,
        blurRadius = 4.px,
        color = rgba(0, 0, 0, 0.24)
    )

    object Level3 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 4.px,
        blurRadius = 6.px,
        color = rgba(0, 0, 0, 0.24)
    )

    object Level4 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 6.px,
        blurRadius = 8.px,
        color = rgba(0, 0, 0, 0.24)
    )

    object Level5 : ShadowElevation(
        offsetX = 0.px,
        offsetY = 8.px,
        blurRadius = 12.px,
        color = rgba(0, 0, 0, 0.24)
    )
}