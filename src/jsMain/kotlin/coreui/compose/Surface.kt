/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import coreui.theme.AppTheme
import coreui.theme.Shape
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun Surface(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    shadowElevation: ShadowElevation = ShadowElevation.Level0,
    tonalElevation: TonalElevation = TonalElevation.Level0,
    contentAlignment: Alignment.Box = Alignment.Box.TopStart,
    content: @Composable () -> Unit
) {
    Box(
        attrs = {
            style {
                backgroundColor(
                    "color-mix(in srgb, ${AppTheme.colors.primary}, ${AppTheme.colors.surface} ${100 - tonalElevation.value}%)"
                )
                color(AppTheme.colors.onSurfaceVariant)
                borderRadius(Shape.extraLarge)
                border {
                    style = LineStyle.Solid
                    color = AppTheme.colors.outline
                    width = 0.px
                }
                filter {
                    dropShadow(
                        offsetX = shadowElevation.offsetX,
                        offsetY = shadowElevation.offsetY,
                        blurRadius = shadowElevation.blurRadius,
                        color = shadowElevation.color
                    )
                }
            }

            applyAttrs(attrs)
        },
        contentAlignment = contentAlignment
    ) {
        content()
    }
}