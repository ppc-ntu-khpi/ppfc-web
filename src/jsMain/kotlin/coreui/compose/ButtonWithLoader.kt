/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.Composable
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLButtonElement

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun ButtonWithLoader(
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
    enabled: Boolean = true,
    loader: Boolean = false,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        attrs = {
            applyAttrs(attrs)
        },
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            attrs = {
                style {
                    transitions {
                        all {
                            duration = 1.s
                            timingFunction = AnimationTimingFunction.EaseOut
                        }
                    }
                }
            },
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalArrangement = Arrangement.Horizontal.Center
        ) {
            if (loader) {
                CircularProgressIndicator(
                    attrs = {
                        style {
                            width(18.px)
                            height(18.px)
                        }
                    },
                    strokeWidth = 3.px,
                    color = AppTheme.colors.onPrimary
                )

                Spacer(width = 8.px)
            }

            content()
        }
    }
}