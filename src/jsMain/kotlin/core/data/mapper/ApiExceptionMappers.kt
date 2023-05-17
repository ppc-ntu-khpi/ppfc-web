package core.data.mapper

import api.*
import core.domain.UnexpectedErrorException

fun ApiException.rethrowDomain(): Exception {
    when (this) {
        is AuthenticationException -> throw onboarding.domain.interactor.AuthenticationException()
        is ChallengeFailedException -> throw onboarding.domain.interactor.ChallengeFailedException()
        is NetworkException -> throw core.domain.NetworkException()
        is TimeoutException -> throw core.domain.TimeoutException()
        else -> throw UnexpectedErrorException()
    }
}