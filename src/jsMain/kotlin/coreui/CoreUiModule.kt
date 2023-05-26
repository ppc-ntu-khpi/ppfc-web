/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui

import coreui.common.ApiCommonErrorMapper
import org.koin.dsl.module

val coreUiModule = module {
    single {
        ApiCommonErrorMapper()
    }
}