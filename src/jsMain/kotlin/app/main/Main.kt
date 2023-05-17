package app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.navigation.NavHost
import app.navigation.Screen
import coreui.compose.BackgroundClip
import coreui.compose.Overflow
import coreui.compose.backgroundClip
import coreui.compose.base.Column
import coreui.compose.overflow
import coreui.theme.AppStyleSheet.style
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.util.CollectUiEvents
import coreui.util.rememberGet
import coreui.util.rememberNavController
import org.jetbrains.compose.web.css.*

@Composable
fun Main() {
    val viewModel: MainViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val navController by rememberNavController<Screen>(root = Screen.Splash)

    CollectUiEvents(
        event = viewState.event,
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    ) { event ->
        when (event) {
            is MainViewEvent.Navigate -> navController.navigate(screen = event.screen)
        }
    }

    AppTheme(
        locale = viewState.preferences.locale,
        colorSchemeMode = viewState.preferences.colorSchemeMode
    ) {
        Column(
            attrs = {
                style {
                    width(100.percent)
                    height(100.vh)
                    overflow(Overflow.Auto)
                    backgroundColor(AppTheme.colors.background)
                }


                "::-webkit-scrollbar" style {
                    width(16.px)
                    height(16.px)
                    backgroundColor(Color.transparent)
                }

                "::-webkit-scrollbar-track" style {
                    backgroundColor(Color.transparent)
                }

                "::-webkit-scrollbar-thumb" style {
                    backgroundColor(AppTheme.colors.primary)
                    backgroundClip(BackgroundClip.ContentBox)
                    borderRadius(Shape.medium)
                    border {
                        color = Color.transparent
                        width = 4.px
                        style = LineStyle.Solid
                    }
                }
            }
        ) {
            NavHost(navController = navController)
        }
    }
}