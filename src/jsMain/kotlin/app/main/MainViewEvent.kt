/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app.main

import app.navigation.Screen

sealed interface MainViewEvent {
    class Navigate(val screen: Screen) : MainViewEvent
}
