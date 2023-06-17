/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

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
fun Courses() {
    val viewModel: CoursesViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedCourses = viewModel.pagedCourses.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedCourses.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is CoursesViewEvent.Message -> uiMessage = event.message
                is CoursesViewEvent.CourseSaved -> viewModel.dialog(dialog = null)
                is CoursesViewEvent.CourseDeleted -> viewModel.dialog(dialog = null)
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
            is CoursesDialog.ManageCourse -> {
                ManageCourseDialog(
                    isLoading = viewState.isSaving,
                    course = dialog.course,
                    onSave = { course ->
                        viewModel.saveCourse(course = course)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is CoursesDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteCourses()
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
                        dialog = CoursesDialog.ManageCourse(course = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = CoursesDialog.ConfirmDeletion(
                            itemsNumber = selectedRowsNumber
                        )
                    )
                },
                enabled = selectedRowsNumber > 0
            ) {
                Text(text = AppTheme.stringResources.tableDelete)
            }

            Spacer(width = 10.px)

            OutlinedNumberField(
                value = viewState.searchQuery.number,
                label = AppTheme.stringResources.coursesSearchLabel,
                
            ) { number ->
                viewModel.setSearchQuery(searchQuery = number)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedCourses,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.coursesNumber)
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
                        viewModel.dialog(dialog = CoursesDialog.ManageCourse(course = item))
                    }
                ) {
                    item {
                        Text(text = item.number.toString())
                    }
                }
            }
        )
    }
}