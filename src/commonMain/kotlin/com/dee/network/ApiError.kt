package com.dee.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Hein Htet
 */
@Serializable
data class ApiErrorResponse(
    val error: ApiError? = null,
)

@Serializable
data class ApiError(
   @SerialName("status_code") val statusCode: Int?,
   @SerialName("status_message") val statusMessage: String?,
    val success: Boolean? = null,
) : Throwable(message = statusMessage)