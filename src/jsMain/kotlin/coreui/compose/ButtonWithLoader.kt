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
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.transitions
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
                    size = 18.px,
                    color = AppTheme.colors.onPrimary
                )

                Spacer(width = 8.px)
            }

            content()
        }
    }
}