/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.users

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.domain.model.User
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.ConfirmDeletionDialog
import tables.presentation.compose.PagingTable
import tables.presentation.compose.item
import tables.presentation.compose.row

@Composable
fun Users() {
    val viewModel: UsersViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedUsers = viewModel.pagedUsers.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedUsers.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is UsersViewEvent.Message -> uiMessage = event.message
                is UsersViewEvent.UserDeleted -> viewModel.dialog(dialog = null)
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

    UiMessageHost(message = uiMessage)

    DialogHost(
        dialog = viewState.dialog
    ) { dialog ->
        when (dialog) {
            is UsersDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteUsers()
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }
        }
    }

    Column(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
                paddingTop(16.px)
                paddingBottom(16.px)
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = UsersDialog.ConfirmDeletion(
                            itemsNumber = selectedRowsNumber
                        )
                    )
                },
                enabled = selectedRowsNumber > 0
            ) {
                Text(text = AppTheme.stringResources.tableDelete)
            }

            Spacer(width = 10.px)

            OutlinedTextField(
                value = viewState.searchQuery.text,
                label = AppTheme.stringResources.usersSearchLabel
            ) { text ->
                viewModel.setSearchQuery(searchQuery = text)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedUsers,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.usersId)
                    }

                    item {
                        Text(text = AppTheme.stringResources.usersUser)
                    }
                }
            },
            body = { _, item ->
                row(
                    isSelected = viewState.rowsSelection[item.id] ?: false,
                    onSelectionChanged = { isSelected ->
                        viewModel.setRowSelection(id = item.id, isSelected = isSelected)
                    }
                ) {
                    item {
                        Text(text = item.id.value.toString())
                    }

                    item {
                        Text(
                            text = when (item) {
                                is User.Group -> "${item.group.number} ${AppTheme.stringResources.usersGroup}"
                                is User.Teacher -> item.teacher.toTextRepresentation()
                            }
                        )
                    }
                }
            }
        )
    }
}