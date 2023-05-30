/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.users

sealed interface UsersDialog {
    class ConfirmDeletion(val itemsNumber: Long) : UsersDialog
}