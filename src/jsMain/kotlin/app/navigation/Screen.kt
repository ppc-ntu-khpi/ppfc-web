/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app.navigation

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object ChangePassword : Screen()
    object Tables : Screen()
}