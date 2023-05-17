/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

sealed class TonalElevation(
    val value: Long
) {
    object Level0 : TonalElevation(value = 0)
    object Level1 : TonalElevation(value = 1)
    object Level2 : TonalElevation(value = 3)
    object Level3 : TonalElevation(value = 6)
    object Level4 : TonalElevation(value = 10)
    object Level5 : TonalElevation(value = 15)
}