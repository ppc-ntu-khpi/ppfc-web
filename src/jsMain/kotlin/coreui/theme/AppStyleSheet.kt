/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

@OptIn(ExperimentalComposeWebApi::class)
@Suppress("SpellCheckingInspection")
object AppStyleSheet : StyleSheet() {

    val circularProgressKeyframes by keyframes {
        from {
            transform {
                rotate(0.deg)
            }
        }

        to {
            transform {
                rotate(360.deg)
            }
        }
    }

    val notSelectable by style {
        property("-webkit-touch-callout", "none")
        property("-webkit-user-select", "none")
        property("-khtml-user-select", "none")
        property("-moz-user-select", "none")
        property("-ms-user-select", "none")
        property("user-select", "none")
    }

    init {
        "body" style {
            margin(0.px)
            fontFamily("Sans-Serif")
        }
    }
}