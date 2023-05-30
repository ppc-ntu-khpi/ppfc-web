/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import tables.domain.model.Group

sealed interface GroupsDialog {
    class ManageGroup(val group: Group?) : GroupsDialog
    class ConfirmDeletion(val itemsNumber: Long) : GroupsDialog
}