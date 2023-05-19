package core.data.mapper

import api.*
import core.domain.UnexpectedErrorException

fun ApiException.toDomain(): core.domain.ApiException {
    return when (this) {
        is AuthenticationException -> onboarding.domain.interactor.AuthenticationException()
        is ChallengeFailedException -> onboarding.domain.interactor.ChallengeFailedException()
        is NetworkException -> core.domain.NetworkException()
        is TimeoutException -> core.domain.TimeoutException()
        else -> UnexpectedErrorException()
    }
}