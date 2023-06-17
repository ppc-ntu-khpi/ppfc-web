/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import tables.domain.model.Change

sealed interface ChangesDialog {
    object CreateChanges : ChangesDialog
    class EditChange(val change: Change) : ChangesDialog
    class ConfirmDeletion(val itemsNumber: Long) : ChangesDialog
    object ConfirmDeletionOfAll : ChangesDialog
}