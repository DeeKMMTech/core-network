@file:Suppress("UNCHECKED_CAST")

package com.dee.network

import com.dee.common.ErrorDisplay
import kotlinx.coroutines.flow.Flow

/**
 * Created by Hein Htet
 */
abstract class BaseUseCase<in Params, T> {
    abstract suspend operator fun invoke(params: Params): Flow<T>

    suspend fun <T> NetworkResult<T>.handleNetworkResult(onSuccess: suspend (data: T) -> Unit) {
        when (this) {
            is NetworkResult.Success<*> -> {
                onSuccess(this.data as T)
            }

            is NetworkResult.Error<*> -> {
                throw ErrorDisplay(this.code, this.message.orEmpty())
            }

            is NetworkResult.Exception<*> -> {
                throw ErrorDisplay(ERROR_COMMON_EXCEPTION_CODE, this.e.message.orEmpty())
            }
        }
    }
}
