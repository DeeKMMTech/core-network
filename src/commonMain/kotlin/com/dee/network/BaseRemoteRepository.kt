package com.dee.network

/**
 * Created by Hein Htet
 */
abstract class BaseRemoteRepository<in P, out T> : Repository<P, T> {
    abstract override suspend fun process(params: P?): NetworkResult<T>
}

interface Repository<in P, out T> {
    suspend fun process(params: P?): NetworkResult<T>
}