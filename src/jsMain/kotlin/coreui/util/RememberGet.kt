/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.koin.compose.getKoin

@Composable
inline fun <reified T : Any> rememberGet(): State<T> {
    val koin = getKoin()
    return remember { mutableStateOf(koin.get<T>()) }
}