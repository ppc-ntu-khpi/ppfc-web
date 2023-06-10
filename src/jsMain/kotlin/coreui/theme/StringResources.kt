/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

import core.domain.model.Locale

fun getStringResources(locale: Locale): StringResources {
    return when(locale) {
        Locale.UA -> uaStringResources
    }
}

data class StringResources(
    val yes: String,
    val no: String,

    val unexpectedErrorException: String,
    val authenticationException: String,
    val networkException: String,
    val timeoutException: String,
    val newPasswordRequiredChallengeFailed: String,
    val formIsNotValidException: String,

    val passwordIsNotInvalid: String,

    val loginTitle: String,
    val loginUsernameFieldLabel: String,
    val loginPasswordFieldLabel: String,
    val loginLogInButton: String,

    val changePasswordTitle: String,
    val changePasswordPasswordFieldLabel: String,
    val changePasswordChangePasswordButton: String,
    val changePasswordLogIn: String,
    val changePasswordInAccount: String,
    val changePasswordShowPassword: String,

    val navigationChanges: String,
    val navigationSchedule: String,
    val navigationDisciplines: String,
    val navigationClassrooms: String,
    val navigationCourses: String,
    val navigationSubjects: String,
    val navigationGroups: String,
    val navigationTeachers: String,
    val navigationUsers: String,

    val tableDialogDeleteTitle: String,
    val tableDialogEditTitle: String,
    val tableRecordsNotFound: String,
    val tableAdd: String,
    val tableDelete: String,
    val tableDeleteRowsWarning: String,
    val tableDeleteRowsConfirm: String,
    val tableDeleteRowsCancel: String,
    val tableManageItemDialogSave: String,
    val tableManageItemDialogCancel: String,

    val classroomsName: String,
    val classroomsSearchLabel: String,

    val coursesNumber: String,
    val coursesSearchLabel: String,

    val disciplinesName: String,
    val disciplinesSearchLabel: String,

    val subjectsName: String,
    val subjectsSearchLabel: String,

    val teachersFirstName: String,
    val teachersLastName: String,
    val teacherMiddleName: String,
    val teachersDiscipline: String,
    val teachersIsHeadTeacher: String,
    val teachersSearchLabel: String,
    val teachersFilterByDisciplineLabel: String,

    val groupsNumber: String,
    val groupsCourseNumber: String,
    val groupsSearchLabel: String,
    val groupsFilterByCourseLabel: String,

    val usersId: String,
    val usersUser: String,
    val usersGroup: String,
    val usersSearchLabel: String,

    val scheduleGroupNumber: String,
    val scheduleClassroomName: String,
    val scheduleTeacher: String,
    val scheduleSubject: String,
    val scheduleEventName: String,
    val scheduleSubjectOrEventName: String,
    val scheduleLessonNumber: String,
    val scheduleDayNumber: String,
    val scheduleWeekAlternation: String,
    val scheduleFilterByGroupLabel: String,
    val scheduleFilterByTeacherLabel: String,
    val scheduleFilterByDayNumber: String,
    val scheduleFilterByWeekAlternation: String,
    val scheduleAddSubject: String,
    val scheduleSubjects: String,

    val dayNumberAll: String,
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String,

    val weekAlternationAll: String,
    val weekAlternationNumerator: String,
    val weekAlternationDenominator: String,
)