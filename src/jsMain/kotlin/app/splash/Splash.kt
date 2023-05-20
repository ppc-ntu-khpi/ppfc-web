/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app.splash

import androidx.compose.runtime.Composable
import coreui.compose.CircularProgressIndicator
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width

@Composable
fun Splash() {
    Box(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
            }
        },
        contentAlignment = Alignment.Box.Center
    ) {
        CircularProgressIndicator()
    }
}