/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

import core.domain.ApiException

class ChallengeFailedException : ApiException(message = "Current authentication challenge is failed.")