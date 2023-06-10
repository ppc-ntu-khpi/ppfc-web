/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.common.mapper

import coreui.theme.AppTheme
import coreui.util.ErrorMapper
import tables.domain.interactor.FormIsNotValidException

class TablesCommonErrorMapper : ErrorMapper {

    override fun map(cause: Throwable): String? {
        return when (cause) {
            is FormIsNotValidException -> AppTheme.stringResources.formIsNotValidException
            else -> null
        }
    }
}