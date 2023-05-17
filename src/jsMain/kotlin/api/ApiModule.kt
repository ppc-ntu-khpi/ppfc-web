/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val apiModule = module {
    single<BearerTokensPersistenceService> {
        BearerTokensPersistenceServiceImpl(
            dataStore = get(parameters = { parametersOf("BEARER_TOKENS") })
        )
    }

    single {
        ApiClient()
    }

    single<AuthService> {
        AuthServiceImpl(get(), get())
    }
}