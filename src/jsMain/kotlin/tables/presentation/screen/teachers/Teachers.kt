/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.presentation.compose.*

@Composable
fun Teachers() {
    val viewModel: TeachersViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedDisciplines = viewModel.pagedDisciplines.collectAsLazyPagingItems()
    val pagedTeachers = viewModel.pagedTeachers.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedTeachers.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is TeachersViewEvent.Message -> uiMessage = event.message
                is TeachersViewEvent.TeacherSaved -> viewModel.dialog(dialog = null)
                is TeachersViewEvent.TeacherDeleted -> viewModel.dialog(dialog = null)
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

    FullscreenProgressIndicator(visible = viewState.isLoading)

    UiMessageHost(message = uiMessage)

    DialogHost(
        dialog = viewState.dialog
    ) { dialog ->
        when (dialog) {
            is TeachersDialog.ManageTeacher -> {
                ManageTeacherDialog(
                    isLoading = viewState.isSaving,
                    teacher = dialog.teacher,
                    onSave = { teacher ->
                        viewModel.saveTeacher(teacher = teacher)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is TeachersDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteTeachers()
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
            Button(
                onClick = {
                    viewModel.dialog(
                        dialog = TeachersDialog.ManageTeacher(teacher = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = TeachersDialog.ConfirmDeletion(
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
                label = AppTheme.stringResources.teachersSearchLabel
            ) { text ->
                viewModel.setSearchQuery(searchQuery = text)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedDisciplines,
                state = viewState.filterDisciplinesMenu,
                label = AppTheme.stringResources.teachersFilterByDisciplineLabel,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                viewModel.setFilterDiscipline(filterDisciplinesMenu = state)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedTeachers,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.teachersLastName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.teachersFirstName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.teacherMiddleName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.teachersDiscipline)
                    }

                    item {
                        Text(text = AppTheme.stringResources.teachersIsHeadTeacher)
                    }
                }
            },
            body = { _, item ->
                row(
                    isSelected = viewState.rowsSelection[item.id] ?: false,
                    onSelectionChanged = { isSelected ->
                        viewModel.setRowSelection(id = item.id, isSelected = isSelected)
                    },
                    onEdit = {
                        viewModel.dialog(dialog = TeachersDialog.ManageTeacher(teacher = item))
                    }
                ) {
                    item {
                        Text(text = item.lastName)
                    }

                    item {
                        Text(text = item.firstName)
                    }

                    item {
                        Text(text = item.middleName)
                    }

                    item {
                        Text(text = item.discipline.name)
                    }

                    item {
                        Text(
                            text = if (item.isHeadTeacher) {
                                AppTheme.stringResources.yes
                            } else {
                                AppTheme.stringResources.no
                            }
                        )
                    }
                }
            }
        )
    }
}