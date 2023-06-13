/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.*
import coreui.compose.base.*
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.LazyPagingItems
import coreui.util.collectAsLazyPagingItems
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.*
import tables.domain.model.*
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.changes.mapper.toDomain
import tables.presentation.screen.changes.model.ChangeState

@Composable
fun CreateChangesDialog(
    isLoading: Boolean,
    onSave: (changes: List<Change>) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: CreateChangesViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val pagedClassrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()
    val pagedTeachers = viewModel.pagedTeachers.collectAsLazyPagingItems()
    val pagedSubjects = viewModel.pagedSubjects.collectAsLazyPagingItems()

    Column(
        attrs = {
            style {
                margin(20.px)
            }
        }
    ) {
        Text(
            text = AppTheme.stringResources.tableDialogEditTitle,
            fontSize = Typography.headlineSmall
        )

        Spacer(height = 16.px)

        Column {
            Row(
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                viewState.changesStates.firstOrNull()?.let { change ->
                    DatePicker(
                        date = change.date
                    ) { date ->
                        viewModel.setDate(date = date)
                    }

                    Spacer(width = 16.px)

                    PagingDropDownMenu(
                        lazyPagingItems = pagedGroups,
                        state = change.group,
                        label = AppTheme.stringResources.changesGroupNumber,
                        itemLabel = { item ->
                            item.number.toString()
                        }
                    ) { state ->
                        viewModel.setGroup(group = state)
                    }
                }

                Spacer(width = 16.px)

                Button(
                    onClick = {
                        viewModel.addChange()
                    },
                    enabled = viewState.canAddItems
                ) {
                    Text(text = AppTheme.stringResources.changesAddSubject)
                }
            }

            Spacer(height = 16.px)

            Text(
                text = AppTheme.stringResources.changesSubject,
                fontSize = 20.px
            )

            Spacer(height = 16.px)

            viewState.changesStates.forEachIndexed { index, change ->
                Change(
                    changeState = change,
                    pagedClassrooms = pagedClassrooms,
                    pagedTeachers = pagedTeachers,
                    pagedSubjects = pagedSubjects,
                    onLessonNumber = {
                        viewModel.setLessonNumber(index = index.toLong(), it)
                    },
                    onWeekAlternation = {
                        viewModel.setWeekAlternation(index = index.toLong(), it)
                    },
                    onClassroom = {
                        viewModel.setClassroom(index = index.toLong(), it)
                    },
                    onTeacher = {
                        viewModel.setTeacher(index = index.toLong(), it)
                    },
                    onSubject = {
                        viewModel.setSubject(index = index.toLong(), it)
                    },
                    onEventName = {
                        viewModel.setEventName(index = index.toLong(), it)
                    },
                    canRemove = viewState.canRemoveItems,
                    onRemove = {
                        viewModel.removeChange(index = index.toLong())
                    }
                )

                Spacer(height = 16.px)

                if (index != viewState.changesStates.lastIndex) {
                    Box(
                        attrs = {
                            style {
                                width(100.percent)
                                height(1.px)
                                backgroundColor(AppTheme.colors.outline)
                            }
                        }
                    )

                    Spacer(height = 16.px)
                }
            }

            Row(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalArrangement = Arrangement.Horizontal.Center
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
                    Text(text = AppTheme.stringResources.tableManageItemDialogCancel)
                }

                Spacer(width = 24.px)

                ButtonWithLoader(
                    attrs = {
                        style {
                            width(100.percent)
                        }
                    },
                    enabled = !(viewState.isFormBlank || isLoading),
                    loader = isLoading,
                    onClick = {
                        onSave(viewState.changesStates.map { it.toDomain() })
                    }
                ) {
                    Text(text = AppTheme.stringResources.tableManageItemDialogSave)
                }
            }
        }
    }
}

@Composable
private fun Change(
    changeState: ChangeState,
    pagedClassrooms: LazyPagingItems<Classroom>,
    pagedTeachers: LazyPagingItems<Teacher>,
    pagedSubjects: LazyPagingItems<Subject>,
    onLessonNumber: (lessonNumber: LessonNumber) -> Unit,
    onWeekAlternation: (weekAlternation: WeekAlternation) -> Unit,
    onClassroom: (classroom: PagingDropDownMenuState<Classroom>) -> Unit,
    onTeacher: (teacher: PagingDropDownMenuState<Teacher>) -> Unit,
    onSubject: (subject: PagingDropDownMenuState<Subject>) -> Unit,
    onEventName: (eventName: String) -> Unit,
    canRemove: Boolean,
    onRemove: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            DropDownMenu(
                items = LessonNumber.values().toList(),
                selectedItem = changeState.lessonNumber,
                label = AppTheme.stringResources.changesLessonNumber,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { item ->
                onLessonNumber(item)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedClassrooms,
                state = changeState.classroom,
                label = AppTheme.stringResources.changesClassroomName,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                onClassroom(state)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedSubjects,
                state = changeState.subject,
                label = AppTheme.stringResources.changesSubject,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                onSubject(state)
            }

            Spacer(width = 16.px)

            IconButton(
                icon = AppIconClass.Delete,
                enabled = canRemove
            ) {
                onRemove()
            }
        }

        Spacer(height = 16.px)

        Row {
            DropDownMenu(
                items = WeekAlternation.values().toList(),
                selectedItem = changeState.weekAlternation,
                label = AppTheme.stringResources.changesWeekAlternation,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { item ->
                onWeekAlternation(item)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedTeachers,
                state = changeState.teacher,
                label = AppTheme.stringResources.changesTeacher,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { state ->
                onTeacher(state)
            }

            Spacer(width = 16.px)

            OutlinedTextField(
                value = changeState.eventName.text,
                label = AppTheme.stringResources.changesEventName
            ) { text ->
                onEventName(text)
            }
        }
    }
}