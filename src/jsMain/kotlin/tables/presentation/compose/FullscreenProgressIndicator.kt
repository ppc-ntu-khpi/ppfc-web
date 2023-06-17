/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import androidx.compose.runtime.Composable
import coreui.compose.CircularProgressIndicator
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import org.jetbrains.compose.web.css.*

@Composable
fun FullscreenProgressIndicator(visible: Boolean) {
    if(!visible) return

    Box(
        attrs = {
            style {
                top(0.px)
                left(0.px)
                width(100.vw)
                height(100.vh)
                backgroundColor(rgba(0, 0, 0, 0.3))
            }
        },
        contentAlignment = Alignment.Box.Center,
        overlapElevation = 1000
    ) {
        CircularProgressIndicator()
    }
}