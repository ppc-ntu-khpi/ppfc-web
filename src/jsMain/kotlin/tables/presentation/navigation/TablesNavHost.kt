/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.navigation

import androidx.compose.runtime.Composable
import coreui.util.NavController
import tables.presentation.screen.changes.Changes
import tables.presentation.screen.classrooms.Classrooms
import tables.presentation.screen.courses.Courses
import tables.presentation.screen.disciplines.Disciplines
import tables.presentation.screen.groups.Groups
import tables.presentation.screen.schedule.Schedule

import tables.presentation.screen.subjects.Subjects
import tables.presentation.screen.teachers.Teachers
import tables.presentation.screen.users.Users

@Composable
fun TablesNavHost(
    navController: NavController<TablesScreen>
) {
    when (navController.currentScreen.value) {
        TablesScreen.Classrooms -> Classrooms()
        TablesScreen.Changes -> Changes()
        TablesScreen.Courses -> Courses()
        TablesScreen.Disciplines -> Disciplines()
        TablesScreen.Groups -> Groups()
        TablesScreen.Schedule -> Schedule()
        TablesScreen.Subjects -> Subjects()
        TablesScreen.Teachers -> Teachers()
        TablesScreen.Users -> Users()
    }
}