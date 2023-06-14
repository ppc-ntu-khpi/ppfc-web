/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

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
import tables.presentation.screen.schedule.model.ScheduleLessonNumberOption
import tables.presentation.screen.schedule.model.ScheduleLessonState

@Composable
fun CreateScheduleItemsDialog(
    isLoading: Boolean,
    onSave: (scheduleItems: List<ScheduleItem>) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: CreateScheduleItemsViewModel by rememberGet()
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
                DropDownMenu(
                    items = DayNumber.values().toList(),
                    selectedItem = viewState.scheduleCommonLesson.dayNumber,
                    label = AppTheme.stringResources.scheduleDayNumber,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setDayNumber(dayNumber = item)
                }

                Spacer(width = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedGroups,
                    state = viewState.scheduleCommonLesson.group,
                    label = AppTheme.stringResources.scheduleGroupNumber,
                    itemLabel = { item ->
                        item.number.toString()
                    }
                ) { state ->
                    viewModel.setGroup(group = state)
                }

                Spacer(width = 16.px)

                Button(
                    onClick = {
                        viewModel.addScheduleItem()
                    },
                    enabled = viewState.canAddLessons
                ) {
                    Text(text = AppTheme.stringResources.scheduleAddSubject)
                }
            }

            Spacer(height = 16.px)

            Text(
                text = AppTheme.stringResources.scheduleLessons,
                fontSize = 20.px
            )

            Spacer(height = 16.px)

            viewState.scheduleLessons.forEach { (id, scheduleLesson) ->
                ScheduleLesson(
                    scheduleLesson = scheduleLesson,
                    pagedClassrooms = pagedClassrooms,
                    pagedTeachers = pagedTeachers,
                    pagedSubjects = pagedSubjects,
                    onLessonNumber = {
                        viewModel.setLessonNumber(id = id, lessonNumber = it)
                    },
                    onWeekAlternation = {
                        viewModel.setWeekAlternation(id = id, weekAlternation = it)
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
                    canRemove = viewState.canRemoveLessons,
                    onRemove = {
                        viewModel.removeScheduleItem(id = id)
                    }
                )

                Spacer(height = 16.px)

                if (id != viewState.scheduleLessons.keys.lastOrNull()) {
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
                        viewModel.getScheduleItems()?.let { scheduleItems ->
                            onSave(scheduleItems)
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
private fun ScheduleLesson(
    scheduleLesson: ScheduleLessonState,
    pagedClassrooms: LazyPagingItems<Classroom>,
    pagedTeachers: LazyPagingItems<Teacher>,
    pagedSubjects: LazyPagingItems<Subject>,
    onLessonNumber: (lessonNumber: ScheduleLessonNumberOption) -> Unit,
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
                items = ScheduleLessonNumberOption.values().toList(),
                selectedItem = scheduleLesson.lessonNumber,
                label = AppTheme.stringResources.scheduleLessonNumber,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { item ->
                onLessonNumber(item)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedClassrooms,
                state = scheduleLesson.classroom,
                label = AppTheme.stringResources.scheduleClassroomName,
                itemLabel = { item ->
                    item.name
                }
            ) { state ->
                onClassroom(state)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedSubjects,
                state = scheduleLesson.subject,
                label = AppTheme.stringResources.scheduleSubject,
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
                selectedItem = scheduleLesson.weekAlternation,
                label = AppTheme.stringResources.scheduleWeekAlternation,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { item ->
                onWeekAlternation(item)
            }

            Spacer(width = 16.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedTeachers,
                state = scheduleLesson.teacher,
                label = AppTheme.stringResources.scheduleTeacher,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { state ->
                onTeacher(state)
            }

            Spacer(width = 16.px)

            OutlinedTextField(
                value = scheduleLesson.eventName.text,
                label = AppTheme.stringResources.scheduleEventName
            ) { text ->
                onEventName(text)
            }
        }
    }
}