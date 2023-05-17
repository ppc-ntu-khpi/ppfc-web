/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app

import app.main.MainViewModel
import org.koin.dsl.module

val appModule = module {
    single {
        MainViewModel(get(), get())
    }
}