/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables

import org.koin.dsl.module
import tables.data.dao.ClassroomsDao
import tables.data.dao.ClassroomsDaoImpl
import tables.data.repository.ClassroomsRepositoryImpl
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.repository.ClassroomsRepository
import tables.presentation.screen.classrooms.ClassroomsViewModel
import tables.presentation.screen.tables.TablesViewModel

val tablesModule = module {
    single<ClassroomsDao> {
        ClassroomsDaoImpl(get())
    }

    single<ClassroomsRepository> {
        ClassroomsRepositoryImpl(get())
    }

    single {
        ObservePagedClassrooms(get())
    }

    factory {
        TablesViewModel(get(), get(), get())
    }

    factory {
        ClassroomsViewModel(get(), get())
    }
}