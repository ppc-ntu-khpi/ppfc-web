/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

import tables.presentation.screen.subjects.model.SubjectState

sealed interface SubjectsDialog {
    class ManageSubject(val subjectState: SubjectState?) : SubjectsDialog
    class ConfirmDeletion(val itemsNumber: Long) : SubjectsDialog
}