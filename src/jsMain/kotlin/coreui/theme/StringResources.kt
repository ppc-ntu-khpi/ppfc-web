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
    val unexpectedErrorException: String,
    val authenticationException: String,
    val networkException: String,
    val timeoutException: String,
    val newPasswordRequiredChallengeFailed: String,

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
)