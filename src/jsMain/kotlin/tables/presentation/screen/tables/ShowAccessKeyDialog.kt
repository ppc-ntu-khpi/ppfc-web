/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.tables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.ButtonWithLoader
import coreui.compose.CircularProgressIndicator
import coreui.compose.OutlinedButton
import coreui.compose.Text
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.rememberGet
import infrastructure.extensions.toISO8601TimeString
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

@Composable
fun ShowAccessKeyDialog(
    onClose: () -> Unit
) {
    val viewModel: AccessKeyViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()

    Column(
        attrs = {
            style {
                width(250.px)
                margin(20.px)
            }
        }
    ) {
        Text(
            text = AppTheme.stringResources.tablesShowAccessKeyTitle,
            fontSize = Typography.headlineSmall
        )

        Spacer(height = 16.px)

        Column(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            if (viewState.errorLoading) {
                Column(
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(text = AppTheme.stringResources.tablesShowAccessKeyErrorLoading)

                    Spacer(height = 5.px)

                    ButtonWithLoader(
                        loader = viewState.isLoading,
                        onClick = {
                            viewModel.generateAccessKey()
                        }
                    ) {
                        Text(text = AppTheme.stringResources.tablesShowAccessKeyRetry)
                    }
                }
            } else if (viewState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = viewState.accessKey.key,
                    fontSize = Typography.displayMedium
                )

                Spacer(height = 5.px)

                Text(
                    text = AppTheme.stringResources.tablesShowAccessKeyExpiresAt.replace(
                        oldValue = "{1}",
                        newValue = viewState.accessKey.expiresAt.toISO8601TimeString(),
                        ignoreCase = true
                    )
                )
            }
        }

        Spacer(height = 16.px)

        OutlinedButton(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            onClick = {
                onClose()
            }
        ) {
            Text(text = AppTheme.stringResources.tablesShowAccessKeyClose)
        }
    }
}