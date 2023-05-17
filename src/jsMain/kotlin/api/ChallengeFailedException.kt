/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

class ChallengeFailedException : ApiException(message = "Current authentication challenge is failed.")