/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.tables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.DialogHost
import coreui.compose.base.Box
import coreui.compose.base.Column
import coreui.util.rememberGet
import coreui.util.rememberNavController
import org.jetbrains.compose.web.css.*
import tables.presentation.compose.TablesTopBar
import tables.presentation.navigation.TablesNavHost
import tables.presentation.navigation.TablesScreen

@Composable
fun Tables() {
    val viewModel: TablesViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val navController by rememberNavController(root = TablesScreen.Changes)

    DialogHost(
        dialog = viewState.dialog
    ) { dialog ->
        when (dialog) {
            is TablesDialog.ShowAccessKey -> {
                ShowAccessKeyDialog(
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }
        }
    }

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
                onGenerateAccessKey = {
                    viewModel.dialog(dialog = TablesDialog.ShowAccessKey)
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