package com.dee.network

import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse

/**
 * Created by Hein Htet
 */
class CustomHttpException(
    response: HttpResponse,
    private val code: String,
    private val errorMessage: String,
) :
    ResponseException(response, errorMessage) {

    val statusCode: String
        get() = code

    override val message: String
        get() = errorMessage
}