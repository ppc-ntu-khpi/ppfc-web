/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

import tables.domain.model.Subject

sealed interface SubjectsDialog {
    class ManageSubject(val subject: Subject?) : SubjectsDialog
    class ConfirmDeletion(val itemsNumber: Long) : SubjectsDialog
}