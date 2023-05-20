/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import core.domain.NetworkException
import core.domain.TimeoutException
import core.domain.UnexpectedErrorException
import core.infrastructure.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.json.Json
import org.ppfc.Web.BuildConfig

class ApiClient {

    private val tag = ApiClient::class.simpleName.toString()

    var client = HttpClient(Js) {
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                val call = try {
                    request.call
                } catch (_: Exception) {
                    null
                }

                val message = """
                    Request failed.
                    Url: ${request.url}.
                    Method: ${request.method.value}.
                    Status code: ${call?.response}.
                    Cause: $cause.
                """.trimIndent().trim()

                Logger.error(tag = tag, message = message)

                when (cause) {
                    is RedirectResponseException  -> throw UnexpectedErrorException()
                    is ClientRequestException -> throw UnexpectedErrorException()
                    is ServerResponseException -> throw UnexpectedErrorException()
                    is NoTransformationFoundException -> throw UnexpectedErrorException()
                    is TimeoutCancellationException -> throw TimeoutException()
                    else -> throw NetworkException()
                }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        defaultRequest {
            url(BuildConfig.API_BASE_ADDRESS)
        }
    }
        private set

    fun config(block: HttpClientConfig<*>.() -> Unit) {
        client = client.config {
            block()
        }
    }
}