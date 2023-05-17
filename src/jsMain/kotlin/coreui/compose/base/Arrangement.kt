/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose.base

import org.jetbrains.compose.web.css.JustifyContent

interface Arrangement {
    sealed class Horizontal(val justifyContent: JustifyContent) {
        object Start : Horizontal(justifyContent = JustifyContent.Start)
        object Center : Horizontal(justifyContent = JustifyContent.Center)
        object End : Horizontal(justifyContent = JustifyContent.End)
        object SpaceBetween : Horizontal(justifyContent = JustifyContent.SpaceBetween)
        object SpaceAround : Horizontal(justifyContent = JustifyContent.SpaceAround)
        object SpaceEvenly : Horizontal(justifyContent = JustifyContent.SpaceEvenly)
    }

    sealed class Vertical(val justifyContent: JustifyContent) {
        object Top : Vertical(justifyContent = JustifyContent.Start)
        object Center : Vertical(justifyContent = JustifyContent.Center)
        object Bottom : Vertical(justifyContent = JustifyContent.End)
        object SpaceBetween : Vertical(justifyContent = JustifyContent.SpaceBetween)
        object SpaceAround : Vertical(justifyContent = JustifyContent.SpaceAround)
        object SpaceEvenly : Vertical(justifyContent = JustifyContent.SpaceEvenly)
    }
}