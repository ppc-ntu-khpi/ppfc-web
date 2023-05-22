/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.tables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.base.Box
import coreui.compose.base.Column
import coreui.util.rememberNavController
import org.jetbrains.compose.web.css.*
import org.koin.compose.getKoin
import tables.presentation.compose.TablesTopBar
import tables.presentation.navigation.TablesNavHost
import tables.presentation.navigation.TablesScreen

@Composable
fun Tables() {
    val viewModel: TablesViewModel = getKoin().get()
    val viewState by viewModel.state.collectAsState()
    val navController by rememberNavController(root = TablesScreen.Classrooms)

    Box(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
            }
        }
    ) {
        Column(
            attrs = {
                style {
                    width(100.percent)
                    height(100.percent)
                    paddingLeft(16.px)
                    paddingRight(16.px)
                }
            }
        ) {
            TablesTopBar(
                attrs = {
                    style {
                        width(100.percent)
                        height(50.px)
                        maxHeight(50.px)
                    }
                },
                selectedScreen = navController.currentScreen.value,
                colorSchemeMode = viewState.preferences.colorSchemeMode,
                onScreenSelected = { screen ->
                    navController.navigate(screen = screen)
                },
                onColorSchemeModeChanged = { colorSchemeMode ->
                    viewModel.setColorSchemeMode(colorSchemeMode = colorSchemeMode)
                },
                onLogOut = {
                    viewModel.logOut()
                }
            )

            Box(
                attrs = {
                    style {
                        width(100.percent)
                        height(100.percent)
                        maxHeight(100.percent - 50.px)
                    }
                }
            ) {
                TablesNavHost(navController = navController)
            }
        }
    }
}