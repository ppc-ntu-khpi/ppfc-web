/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

class ChallengeFailedException : Exception(message = "Current authentication challenge is failed.")