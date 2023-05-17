/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import api.mapper.toKtorBearerTokens
import api.model.*
import api.model.BearerTokens
import core.infrastructure.Logger
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class AuthServiceImpl(
    private val apiClient: ApiClient,
    private val bearerTokensPersistenceService: BearerTokensPersistenceService
) : AuthService {

    private val tag = AuthServiceImpl::class.simpleName.toString()

    private var currentChallengeState = MutableStateFlow<ChallengeState?>(null)

    init {
        apiClient.config {
            install(Auth) {
                bearer {
                    sendWithoutRequest { true }

                    loadTokens {
                        val bearerTokens = bearerTokensPersistenceService.getBearerTokens()?.toKtorBearerTokens()
                        Logger.debug(tag = tag, message = "Local bearer tokens are loaded '${bearerTokens != null}'.")
                        return@loadTokens bearerTokens
                    }

                    refreshTokens {
                        val bearerTokens = refreshBearerTokens()?.toKtorBearerTokens()
                        Logger.debug(tag = tag, message = "Bearer tokens are refreshed '${bearerTokens != null}'.")
                        bearerTokens ?: logOut()
                        return@refreshTokens bearerTokens
                    }
                }
            }
        }
    }

    private suspend fun refreshBearerTokens(): BearerTokens? {
        val currentBearerTokens = bearerTokensPersistenceService.getBearerTokens() ?: return null

        val authResponse = apiClient.client.post("refreshAccessToken") {
            contentType(ContentType.Application.Json)
            setBody(
                RefreshAccessTokenRequest(refreshToken = currentBearerTokens.refreshToken)
            )
        }

        if (authResponse.status != HttpStatusCode.OK) {
            Logger.error(tag = tag, message = "Failed to request refreshed bearer tokens.")
            return null
        }

        val authResponseBody: AuthResponse = try {
            authResponse.body()
        } catch (e: NoTransformationFoundException) {
            Logger.error(
                tag = tag,
                message = "Failed to deserialize refreshed bearer tokens response."
            )
            return null
        }

        if(authResponseBody.status == AuthResponseStatus.FAILURE) return null

        val bearerTokens = BearerTokens(
            accessToken = authResponseBody.accessToken,
            refreshToken = authResponseBody.refreshToken
        )

        bearerTokensPersistenceService.saveBearerTokens(
            bearerTokens = bearerTokens
        )

        return bearerTokens
    }

    override fun observeAuthState(): Flow<AuthState> = bearerTokensPersistenceService.observeBearerTokens()
        .combine(currentChallengeState) { bearerTokens, challengeState ->
            when {
                bearerTokens != null -> AuthState.LOGGED_IN
                challengeState is ChallengeState.NewPasswordRequired -> AuthState.NEW_PASSWORD_REQUIRED
                else -> AuthState.LOGGED_OUT
            }
        }

    override suspend fun logIn(credentials: AuthCredentials) {
        val authResponse = apiClient.client.post("authenticate") {
            contentType(ContentType.Application.Json)
            setBody(
                AuthRequest(
                    username = credentials.username,
                    password = credentials.password
                )
            )
        }

        if (authResponse.status != HttpStatusCode.OK) {
            Logger.error(tag = tag, message = "Failed to request authentication.")
            throw AuthenticationException()
        }

        val authResponseBody: AuthResponse = try {
            authResponse.body()
        } catch (e: NoTransformationFoundException) {
            Logger.error(
                tag = tag,
                message = "Failed to deserialize authentication response."
            )
            throw AuthenticationException()
        }

        when (authResponseBody.status) {
            AuthResponseStatus.SUCCESS -> {
                Logger.debug(
                    tag = tag,
                    message = "Successfully authenticated."
                )

                bearerTokensPersistenceService.saveBearerTokens(
                    bearerTokens = BearerTokens(
                        accessToken = authResponseBody.accessToken,
                        refreshToken = authResponseBody.refreshToken
                    )
                )
            }

            AuthResponseStatus.FAILURE -> {
                Logger.error(
                    tag = tag,
                    message = "Failed to authenticate due to '${authResponseBody.error}'."
                )

                throw AuthenticationException()
            }

            AuthResponseStatus.NEW_PASSWORD_REQUIRED -> {
                Logger.debug(
                    tag = tag,
                    message = "Failed to authenticate due to 'NEW_PASSWORD_REQUIRED' challenge."
                )

                currentChallengeState.value = ChallengeState.NewPasswordRequired(
                    username = credentials.username,
                    session = authResponseBody.session
                )
            }
        }
    }

    override fun logOut() {
        currentChallengeState.value = null
        bearerTokensPersistenceService.clear()
    }

    override suspend fun passNewPasswordRequiredChallenge(password: String) {
        val challengeState = currentChallengeState.value ?: throw ChallengeFailedException()

        val authResponse = apiClient.client.post("authNewPasswordRequiredChallenge") {
            contentType(ContentType.Application.Json)
            setBody(
                AuthNewPasswordRequiredChallengeRequest(
                    username = challengeState.username,
                    password = password,
                    session = challengeState.session
                )
            )
        }

        if (authResponse.status != HttpStatusCode.OK) {
            Logger.error(tag = tag, message = "Failed to pass 'NEW_PASSWORD_REQUIRED' challenge.")
            throw ChallengeFailedException()
        }

        val authResponseBody: AuthResponse = try {
            authResponse.body()
        } catch (e: NoTransformationFoundException) {
            Logger.error(
                tag = tag,
                message = "Failed to deserialize 'NEW_PASSWORD_REQUIRED' challenge response."
            )
            throw ChallengeFailedException()
        }

        if (authResponseBody.status == AuthResponseStatus.SUCCESS) {
            Logger.debug(
                tag = tag,
                message = "Successfully passed 'NEW_PASSWORD_REQUIRED' challenge."
            )

            currentChallengeState.value = null

            bearerTokensPersistenceService.saveBearerTokens(
                bearerTokens = BearerTokens(
                    accessToken = authResponseBody.accessToken,
                    refreshToken = authResponseBody.refreshToken
                )
            )
        } else {
            Logger.error(
                tag = tag,
                message = "Failed to pass 'NEW_PASSWORD_REQUIRED' challenge due to '${authResponseBody.error}'."
            )

            throw ChallengeFailedException()
        }
    }
}