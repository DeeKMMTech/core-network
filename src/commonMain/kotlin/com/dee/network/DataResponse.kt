package com.dee.network

import kotlinx.serialization.Serializable


class UnexpectedException(cause: Exception) : Exception(cause)

@Serializable
data class BaseResponse<Data>(
    val code: String,
    val message: String? = null,
    val response: Data? = null,
)