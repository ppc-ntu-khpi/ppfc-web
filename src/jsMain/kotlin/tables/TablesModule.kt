/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables

import org.koin.dsl.module
import tables.data.dao.*
import tables.data.repository.ClassroomsRepositoryImpl
import tables.data.repository.CoursesRepositoryImpl
import tables.data.repository.DisciplinesRepositoryImpl
import tables.data.repository.SubjectsRepositoryImpl
import tables.domain.interactor.*
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedCourses
import tables.domain.observer.ObservePagedDisciplines
import tables.domain.observer.ObservePagedSubjects
import tables.domain.repository.ClassroomsRepository
import tables.domain.repository.CoursesRepository
import tables.domain.repository.DisciplinesRepository
import tables.domain.repository.SubjectsRepository
import tables.presentation.screen.classrooms.ClassroomsViewModel
import tables.presentation.screen.classrooms.ManageClassroomViewModel
import tables.presentation.screen.courses.CoursesViewModel
import tables.presentation.screen.courses.ManageCourseViewModel
import tables.presentation.screen.disciplines.DisciplinesViewModel
import tables.presentation.screen.disciplines.ManageDisciplineViewModel
import tables.presentation.screen.subjects.ManageSubjectViewModel
import tables.presentation.screen.subjects.SubjectsViewModel
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

    /* Disciplines */
    single<DisciplinesDao> {
        DisciplinesDaoImpl(get())
    }

    single<DisciplinesRepository> {
        DisciplinesRepositoryImpl(get())
    }

    single {
        SaveDiscipline(get())
    }

    single {
        DeleteDisciplines(get())
    }

    single {
        ObservePagedDisciplines(get())
    }

    factory {
        DisciplinesViewModel(get(), get(), get(), get())
    }

    factory {
        ManageDisciplineViewModel()
    }

    /* Subjects */
    single<SubjectsDao> {
        SubjectsDaoImpl(get())
    }

    single<SubjectsRepository> {
        SubjectsRepositoryImpl(get())
    }

    single {
        SaveSubject(get())
    }

    single {
        DeleteSubjects(get())
    }

    single {
        ObservePagedSubjects(get())
    }

    factory {
        SubjectsViewModel(get(), get(), get(), get())
    }

    factory {
        ManageSubjectViewModel()
    }
}