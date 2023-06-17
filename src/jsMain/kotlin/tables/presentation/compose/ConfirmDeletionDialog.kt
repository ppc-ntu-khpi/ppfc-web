/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.compose

import androidx.compose.runtime.Composable
import coreui.compose.ButtonWithLoader
import coreui.compose.OutlinedButton
import coreui.compose.Text
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Column
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.theme.Typography
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

@Composable
fun ConfirmDeletionDialog(
    isLoading: Boolean,
    itemsNumber: Long? = null,
    onConfirm: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        attrs = {
            style {
                width(250.px)
                margin(20.px)
            }
        }
    ) {
        Text(
            text = AppTheme.stringResources.tableDialogDeleteTitle,
            fontSize = Typography.headlineSmall
        )

        Spacer(height = 16.px)

        val warning = if(itemsNumber == null) {
            AppTheme.stringResources.tableDeleteAllRowsWarning
        } else {
            AppTheme.stringResources.tableDeleteRowsWarning.replace("{1}", itemsNumber.toString())
        }
        Text(text = warning)

        Spacer(height = 16.px)

        Column(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalArrangement = Arrangement.Vertical.Center
        ) {
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
                Text(text = AppTheme.stringResources.tableDeleteRowsCancel)
            }

            Spacer(height = 16.px)

            ButtonWithLoader(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                enabled = !isLoading,
                loader = isLoading,
                onClick = {
                    onConfirm()
                }
            ) {
                Text(text = AppTheme.stringResources.tableDeleteRowsConfirm)
            }
        }
    }
}