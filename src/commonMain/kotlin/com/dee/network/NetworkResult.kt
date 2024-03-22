package com.dee.network

sealed class NetworkResult<out T> {
    class Success<T>(val data: T) : NetworkResult<T>()
    class Error<T>(val code: String, val message: String?) : NetworkResult<T>()
    class Exception<T>(val e: Throwable) : NetworkResult<T>()
}


const val ERROR_COMMON_EXCEPTION_CODE = "1"
