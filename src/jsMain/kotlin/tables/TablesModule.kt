/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables

import org.koin.dsl.module
import tables.data.dao.ClassroomsDao
import tables.data.dao.ClassroomsDaoImpl
import tables.data.dao.CoursesDao
import tables.data.dao.CoursesDaoImpl
import tables.data.repository.ClassroomsRepositoryImpl
import tables.data.repository.CoursesRepositoryImpl
import tables.domain.interactor.DeleteClassrooms
import tables.domain.interactor.DeleteCourses
import tables.domain.interactor.SaveClassroom
import tables.domain.interactor.SaveCourse
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedCourses
import tables.domain.repository.ClassroomsRepository
import tables.domain.repository.CoursesRepository
import tables.presentation.screen.classrooms.ClassroomsViewModel
import tables.presentation.screen.classrooms.ManageClassroomViewModel
import tables.presentation.screen.courses.CoursesViewModel
import tables.presentation.screen.courses.ManageCourseViewModel
import tables.presentation.screen.tables.TablesViewModel

val tablesModule = module {
    factory {
        TablesViewModel(get(), get(), get())
    }

    /* Classrooms */
    single<ClassroomsDao> {
        ClassroomsDaoImpl(get())
    }

    single<ClassroomsRepository> {
        ClassroomsRepositoryImpl(get())
    }

    single {
        SaveClassroom(get())
    }

    single {
        DeleteClassrooms(get())
    }

    single {
        ObservePagedClassrooms(get())
    }

    factory {
        ClassroomsViewModel(get(), get(), get(), get())
    }

    factory {
        ManageClassroomViewModel()
    }

    /* Courses */
    single<CoursesDao> {
        CoursesDaoImpl(get())
    }

    single<CoursesRepository> {
        CoursesRepositoryImpl(get())
    }

    single {
        SaveCourse(get())
    }

    single {
        DeleteCourses(get())
    }

    single {
        ObservePagedCourses(get())
    }

    factory {
        CoursesViewModel(get(), get(), get(), get())
    }

    factory {
        ManageCourseViewModel()
    }
}