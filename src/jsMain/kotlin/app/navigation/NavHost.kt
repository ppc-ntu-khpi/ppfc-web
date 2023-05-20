/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app.navigation

import androidx.compose.runtime.Composable
import app.splash.Splash
import coreui.util.NavController
import onboarding.presentation.screen.changepassword.ChangePassword
import onboarding.presentation.screen.login.Login
import tables.presentation.screen.tables.Tables

@Composable
fun NavHost(
    navController: NavController<Screen>
) {
    when (navController.currentScreen.value) {
        Screen.Splash -> Splash()
        Screen.Login -> Login()
        Screen.ChangePassword -> ChangePassword()
        Screen.Tables -> Tables()
    }
}