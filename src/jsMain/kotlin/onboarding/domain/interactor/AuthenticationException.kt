package onboarding.domain.interactor

import core.domain.ApiException

class AuthenticationException : ApiException(message = "Incorrect username or password.")