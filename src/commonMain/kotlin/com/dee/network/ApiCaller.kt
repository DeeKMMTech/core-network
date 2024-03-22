package com.dee.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request

/**
 * Created by Hein Htet
 */
suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): NetworkResult<T> =
    try {
        val response = request { block() }
        NetworkResult.Success(response.body())
    } catch (e: CustomHttpException) {
        NetworkResult.Error(code = e.statusCode, message = e.message)
    } catch (e: ServerResponseException) {
        NetworkResult.Error(code = e.response.status.value.toString(), message = e.message)
    } catch (e: ClientRequestException) {
        NetworkResult.Error(code = e.response.status.value.toString(), message = e.response.body())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }