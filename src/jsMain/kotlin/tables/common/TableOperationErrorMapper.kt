/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.common

import core.domain.NetworkException
import core.domain.TimeoutException
import coreui.theme.AppTheme
import coreui.util.ErrorMapper

open class TableOperationErrorMapper : ErrorMapper {

    override fun map(cause: Throwable): String? {
        return when (cause) {
            is NetworkException -> AppTheme.stringResources.networkException
            is TimeoutException -> AppTheme.stringResources.timeoutException
            else -> AppTheme.stringResources.unexpectedErrorException
        }
    }
}