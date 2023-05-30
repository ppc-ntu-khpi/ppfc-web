/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import tables.domain.model.Discipline

sealed interface DisciplinesDialog {
    class ManageDiscipline(val discipline: Discipline?) : DisciplinesDialog
    class ConfirmDeletion(val itemsNumber: Long) : DisciplinesDialog
}