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
import coreui.theme.Shape
import coreui.theme.Typography
import coreui.util.LazyPagingItems
import coreui.util.collectAsLazyPagingItems
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.*
import tables.domain.model.*
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.changes.mapper.toTextRepresentation
import tables.presentation.screen.changes.model.ChangeLessonNumberOption
import tables.presentation.screen.changes.model.ChangeLessonState

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
                DatePicker(
                    date = viewState.changesCommonLesson.date
                ) { date ->
                    viewModel.setDate(date = date)
                }

                Spacer(width = 16.px)

                DropDownMenu(
                    items = DayNumber.values().toList(),
                    selectedItem = viewState.changesCommonLesson.dayNumber,
                    label = AppTheme.stringResources.changesDayNumber,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setDayNumber(dayNumber = item)
                }

                Spacer(width = 16.px)

                DropDownMenu(
                    items = WeekAlternation.values().toList(),
                    selectedItem = viewState.changesCommonLesson.weekAlternation,
                    label = AppTheme.stringResources.changesWeekAlternation,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setWeekAlternation(item)
                }

                Spacer(width = 16.px)

                Button(
                    onClick = {
                        viewModel.addChange()
                    },
                    enabled = viewState.canAddLessons
                ) {
                    Text(text = AppTheme.stringResources.changesAddSubject)
                }
            }

            Spacer(height = 16.px)

            Text(
                text = AppTheme.stringResources.changesLessons,
                fontSize = 20.px
            )

            Spacer(height = 16.px)

            viewState.changesLessons.forEach { (id, changeLesson) ->
                ChangeLesson(
                    changeLesson = changeLesson,
                    pagedGroups = pagedGroups,
                    pagedClassrooms = pagedClassrooms,
                    pagedTeachers = pagedTeachers,
                    pagedSubjects = pagedSubjects,
                    onLessonNumber = {
                        viewModel.setLessonNumber(id = id, lessonNumber = it)
                    },
                    onGroup = {
                        viewModel.setGroup(id = id, group = it)
                    },
                    onClassroom = {
                        viewModel.setClassroom(id = id, classroom = it)
                    },
                    onTeacher = {
                        viewModel.setTeacher(id = id, teacher = it)
                    },
                    onSubject = {
                        viewModel.setSubject(id = id, subject = it)
                    },
                    onEventName = {
                        viewModel.setEventName(id = id, eventName = it)
                    },
                    onAddGroup = {
                        viewModel.addGroup(id = id, group = it)
                    },
                    onRemoveGroup = {
                        viewModel.removeGroup(id = id, group = it)
                    },
                    canRemove = viewState.canRemoveLessons,
                    canAddGroups = viewState.canAddGroups[id] ?: false,
                    onRemove = {
                        viewModel.removeChange(id = id)
                    }
                )

                Spacer(height = 16.px)

                if (id != viewState.changesLessons.keys.lastOrNull()) {
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
                        viewModel.getChanges()?.let { changes ->
                            onSave(changes)
                        }
                    }
                ) {
                    Text(text = AppTheme.stringResources.tableManageItemDialogSave)
                }
            }
        }
    }
}

@Composable
private fun ChangeLesson(
    changeLesson: ChangeLessonState,
    pagedGroups: LazyPagingItems<Group>,
    pagedClassrooms: LazyPagingItems<Classroom>,
    pagedTeachers: LazyPagingItems<Teacher>,
    pagedSubjects: LazyPagingItems<Subject>,
    onLessonNumber: (lessonNumber: ChangeLessonNumberOption) -> Unit,
    onGroup: (group: PagingDropDownMenuState<Group>) -> Unit,
    onClassroom: (classroom: PagingDropDownMenuState<Classroom>) -> Unit,
    onTeacher: (teacher: PagingDropDownMenuState<Teacher>) -> Unit,
    onSubject: (subject: PagingDropDownMenuState<Subject>) -> Unit,
    onEventName: (eventName: String) -> Unit,
    onAddGroup: (group: Group) -> Unit,
    onRemoveGroup: (group: Group) -> Unit,
    canRemove: Boolean,
    canAddGroups: Boolean,
    onRemove: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            PagingDropDownMenu(
                lazyPagingItems = pagedGroups,
                state = changeLesson.group,
                enabled = canAddGroups,
                label = AppTheme.stringResources.changesGroupNumber,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                onGroup(state)
                if (state.selectedItem == null || state.selectedItem == changeLesson.group.selectedItem)
                    return@PagingDropDownMenu
                onAddGroup(state.selectedItem)
            }

            Spacer(width = 16.px)

            Row(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        flexWrap(FlexWrap.Wrap)
                        maxWidth(400.px)
                    }
                }
            ) groupsRow@{
                changeLesson.selectedGroups.forEach { selectedGroup ->
                    Text(
                        attrs = {
                            style {
                                backgroundColor(AppTheme.colors.primary)
                                color(AppTheme.colors.onPrimary)
                                padding(5.px)
                                margin(4.px)
                                borderRadius(Shape.large)
                            }

                            onClick {
                                onRemoveGroup(selectedGroup)
                            }
                        },
                        text = selectedGroup.number.toString(),
                        fontSize = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (changeLesson.selectedGroups.isNotEmpty()) return@groupsRow

                Text(text = AppTheme.stringResources.createChangesGroupIsNotSelected)
            }
        }

        Spacer(height = 16.px)

        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            DropDownMenu(
                items = ChangeLessonNumberOption.values().toList(),
                selectedItem = changeLesson.lessonNumber,
                label = AppTheme.stringResources.changesLessonNumber,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { item ->
                onLessonNumber(item)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedSubjects,
                state = changeLesson.subject,
                label = AppTheme.stringResources.changesSubject,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                onSubject(state)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedClassrooms,
                state = changeLesson.classroom,
                label = AppTheme.stringResources.changesClassroomName,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                onClassroom(state)
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
            PagingDropDownMenu(
                lazyPagingItems = pagedTeachers,
                state = changeLesson.teacher,
                label = AppTheme.stringResources.changesTeacher,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { state ->
                onTeacher(state)
            }

            Spacer(width = 16.px)

            OutlinedTextField(
                value = changeLesson.eventName.text,
                label = AppTheme.stringResources.changesEventName
            ) { text ->
                onEventName(text)
            }
        }
    }
}