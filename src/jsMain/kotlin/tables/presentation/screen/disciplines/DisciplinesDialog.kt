/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import tables.presentation.screen.disciplines.model.DisciplineState

sealed interface DisciplinesDialog {
    class ManageDiscipline(val disciplineState: DisciplineState?) : DisciplinesDialog
    class ConfirmDeletion(val itemsNumber: Long) : DisciplinesDialog
}