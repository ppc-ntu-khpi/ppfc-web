package tables.presentation.navigation

import coreui.theme.AppTheme

enum class TablesScreen {
    Changes,
    Schedule,
    Disciplines,
    Classrooms,
    Courses,
    Subjects,
    Groups,
    Teachers,
    Users
}

fun TablesScreen.getName(): String {
    return when (this) {
        TablesScreen.Changes -> AppTheme.stringResources.navigationChanges
        TablesScreen.Schedule -> AppTheme.stringResources.navigationSchedule
        TablesScreen.Disciplines -> AppTheme.stringResources.navigationDisciplines
        TablesScreen.Classrooms -> AppTheme.stringResources.navigationClassrooms
        TablesScreen.Courses -> AppTheme.stringResources.navigationCourses
        TablesScreen.Subjects -> AppTheme.stringResources.navigationSubjects
        TablesScreen.Groups -> AppTheme.stringResources.navigationGroups
        TablesScreen.Teachers -> AppTheme.stringResources.navigationTeachers
        TablesScreen.Users -> AppTheme.stringResources.navigationUsers
    }
}