/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import androidx.compose.runtime.*
import kotlinx.atomicfu.locks.synchronized

class NavController<T>(
    root: T
) {
    private var backStack by mutableStateOf(setOf(root))
    val currentScreen = derivedStateOf {
        backStack.lastOrNull() ?: root
    }

    fun navigate(screen: T) {
        synchronized(this) {
            val screenIndex = backStack.indexOf(screen)

            if(screenIndex == -1) {
                backStack += screen
            } else {
                backStack = backStack.take(screenIndex + 1).toSet()
            }
        }
    }

    fun popBackStack() {
        synchronized(this) {
            if (backStack.size <= 1) return
            backStack -= backStack.last()
        }
    }
}

@Composable
fun <T> rememberNavController(
    root: T
) = remember {
    mutableStateOf(NavController(root = root))
}