/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables

import org.koin.dsl.module
import tables.data.dao.ClassroomsDao
import tables.data.dao.ClassroomsDaoImpl
import tables.data.repository.ClassroomsRepositoryImpl
import tables.domain.interactor.DeleteClassrooms
import tables.domain.interactor.SaveClassroom
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.repository.ClassroomsRepository
import tables.presentation.screen.classrooms.ClassroomsViewModel
import tables.presentation.screen.classrooms.ManageClassroomViewModel
import tables.presentation.screen.tables.TablesViewModel

val tablesModule = module {
    factory {
        TablesViewModel(get(), get(), get())
    }

    /** Classrooms */
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
}