package com.dee.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Created by Hein Htet
 */

fun provideHttpClient(): HttpClient {
    val httpClient = HttpClient {
        defaultRequest {
//            url("https://api.punkapi.com/v2/")
            url("https://api.themoviedb.org/3/")
        }
        addMiddleware()
        addLogging()
        addContentNegotiation()
    }
    return httpClient
}

private val json = Json { ignoreUnknownKeys = true }

fun HttpClientConfig<*>.addMiddleware() {
    install(ResponseObserver) {
        onResponse { response ->
            println("HTTP status: ${response.status.value}")
        }
    }
    HttpResponseValidator {
        validateResponse { response: HttpResponse ->
            val statusCode = response.status.value
            println("HTTP status: $statusCode BODY ")
            println("HTTP Response: $response")

            // handle http error, if any error throw to custom http exception
            response.handleHttpError(statusCode)

            when (statusCode) {
                in 300..399 -> throw RedirectResponseException(response, "")
                in 400..499 -> throw ClientRequestException(response, "")
                in 500..599 -> throw ServerResponseException(response, "")
            }
            if (statusCode >= 600) {
                throw ResponseException(response, "")
            }
        }
    }
}


suspend fun HttpResponse.handleHttpError(statusCode: Int) {
//    println("HTTP ERROR ${this.body<String>()}")
    if (statusCode != 200) {
        try {
            val errorResponse = json.decodeFromString<ApiError>(this.body<String>())
            if (errorResponse.statusMessage != null) {
                throw CustomHttpException(
                    this.body(),
                    errorResponse.statusCode.toString(), errorResponse.statusMessage
                )
            }
        } catch (e: Throwable) {
            throw e
        }
    }
}

fun HttpClientConfig<*>.addLogging() {
    return install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("HTTP call ----> $message")
            }
        }
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}


fun HttpClientConfig<*>.addContentNegotiation() {
    install(ContentNegotiation) {
        register(
            ContentType.Text.Html, KotlinxSerializationConverter(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        )
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}