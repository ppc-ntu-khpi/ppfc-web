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

    val passwordIsInvalid: String,

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
    val navigationUsers: String
)