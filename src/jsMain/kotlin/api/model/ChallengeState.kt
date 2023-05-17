/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.model

sealed class ChallengeState(
    val username: String,
    val session: String
) {
    class NewPasswordRequired(username: String, session: String) :
        ChallengeState(username = username, session = session)
}