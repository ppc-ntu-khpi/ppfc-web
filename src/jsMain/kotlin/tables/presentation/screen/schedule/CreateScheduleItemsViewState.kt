/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import tables.domain.model.Id
import tables.presentation.screen.schedule.model.ScheduleCommonLessonState
import tables.presentation.screen.schedule.model.ScheduleLessonState

data class CreateScheduleItemsViewState(
    val scheduleCommonLesson: ScheduleCommonLessonState = ScheduleCommonLessonState.Empty,
    val scheduleLessons: Map<Id.Value, ScheduleLessonState> = mapOf(
        Id.random() to ScheduleLessonState.Empty
    ),
    val isFormBlank: Boolean = true,
    val canAddLessons: Boolean = true,
    val canRemoveLessons: Boolean = false
) {
    companion object {
        val Empty = CreateScheduleItemsViewState()
    }
}