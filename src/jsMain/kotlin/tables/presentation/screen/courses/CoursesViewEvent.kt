/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import coreui.util.UiMessage

sealed interface CoursesViewEvent {
    class Message(val message: UiMessage) : CoursesViewEvent
    object CourseSaved : CoursesViewEvent
    object CourseDeleted : CoursesViewEvent
}