/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.presentation.screen.login

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Box
import coreui.compose.base.Column
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.CollectUiEvents
import coreui.util.UiMessage
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.*

@Composable
fun Login() {
    val viewModel: LoginViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }

    CollectUiEvents(
        event = viewState.event,
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    ) { event ->
        when(event) {
            is LoginViewEvent.Message -> uiMessage = event.message
        }
    }

    UiMessageHost(message = uiMessage)

    Box(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
                backgroundColor(AppTheme.colors.background)
            }
        },
        contentAlignment = Alignment.Box.Center
    ) {
        Surface(
            attrs = {
                style {
                    width(250.px)
                    padding(20.px)
                }
            },
            shadowElevation = ShadowElevation.Level3,
            tonalElevation = TonalElevation.Level3
        ) {
            Column(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
            ) {
                Text(
                    text = AppTheme.stringResources.loginTitle,
                    fontSize = Typography.headlineSmall
                )

                Spacer(height = 10.px)

                OutlinedTextField(
                    attrs = {
                        style {
                            width(100.percent)
                        }
                    },
                    value = viewState.username.text,
                    error =viewState.username.error,
                    label = AppTheme.stringResources.loginUsernameFieldLabel
                ) { text ->
                    viewModel.setUsername(
                        username = viewState.password.copy(text = text)
                    )
                }

                Spacer(height = 10.px)

                OutlinedTextField(
                    attrs = {
                        style {
                            width(100.percent)
                        }
                    },
                    value = viewState.password.text,
                    error = viewState.password.error,
                    label = AppTheme.stringResources.loginPasswordFieldLabel
                ) { text ->
                    viewModel.setPassword(
                        password = viewState.password.copy(text = text)
                    )
                }

                Spacer(height = 18.px)

                ButtonWithLoader(
                    attrs = {
                        style {
                            width(100.percent)
                        }
                    },
                    enabled = !(viewState.isFormBlank || viewState.isLoading),
                    loader = viewState.isLoading,
                    onClick = {
                        viewModel.logIn()
                    }
                ) {
                    Text(
                        text = AppTheme.stringResources.loginLogInButton
                    )
                }
            }
        }
    }
}