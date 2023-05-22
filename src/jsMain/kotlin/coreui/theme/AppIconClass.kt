/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

sealed class AppIconClass(val value: String) {
    object Edit : AppIconClass(value = "fas fa-pen")
    object LightColorScheme : AppIconClass(value = "fas fa-sun")
    object DarkColorScheme : AppIconClass(value = "fas fa-moon")
    object LogOut : AppIconClass(value = "fa fa-sign-out")
    object EmptyTable : AppIconClass(value = "fa-solid fa-file-lines")
    object Check : AppIconClass(value = "fa-solid fa-check")
    object Refresh : AppIconClass(value = "fa-solid fa-refresh")
}