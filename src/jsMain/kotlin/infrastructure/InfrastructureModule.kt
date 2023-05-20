/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package infrastructure

import core.infrastructure.DataStore
import org.koin.dsl.module

val infrastructureModule = module {
    factory<DataStore> { params ->
        AppDataStore(params.get())
    }
}