/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding

import onboarding.data.dao.AuthDao
import onboarding.data.dao.AuthDaoImpl
import onboarding.data.repository.AuthRepositoryImpl
import onboarding.domain.interactor.LogIn
import onboarding.domain.interactor.LogOut
import onboarding.domain.interactor.PassNewPasswordRequiredChallenge
import onboarding.domain.interactor.ValidatePassword
import onboarding.domain.repository.AuthRepository
import onboarding.presentation.screen.changepassword.ChangePasswordViewModel
import onboarding.presentation.screen.login.LoginViewModel
import org.koin.dsl.module

val onboardingModule = module {
    single<AuthDao> {
        AuthDaoImpl(get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }

    single {
        LogIn(get())
    }

    single {
        LogOut(get())
    }

    single {
        ValidatePassword()
    }

    single {
        PassNewPasswordRequiredChallenge(get(), get())
    }

    factory {
        LoginViewModel(get(), get())
    }

    factory {
        ChangePasswordViewModel(get(), get(), get())
    }
}