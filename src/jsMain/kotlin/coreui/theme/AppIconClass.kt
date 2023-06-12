/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

sealed class AppIconClass(val value: String) {
    object Edit : AppIconClass(value = "fas fa-pen")
    object Delete : AppIconClass(value = "fas fa-trash")
    object LightColorScheme : AppIconClass(value = "fas fa-sun")
    object DarkColorScheme : AppIconClass(value = "fas fa-moon")
    object LogOut : AppIconClass(value = "fa fa-sign-out")
    object EmptyTable : AppIconClass(value = "fa-solid fa-file-lines")
    object Check : AppIconClass(value = "fa-solid fa-check")
    object Refresh : AppIconClass(value = "fa-solid fa-refresh")
    object ArrowUp : AppIconClass(value = "fa-solid fa-angle-up")
    object ArrowDown : AppIconClass(value = "fa-solid fa-angle-down")
    object Cancel : AppIconClass(value = "fa-solid fa-xmark")
    object Calendar : AppIconClass(value = "fa-solid fa-calendar")
}