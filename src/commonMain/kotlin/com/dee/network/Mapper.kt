package com.dee.network

/**
 * Created by Hein Htet
 */
interface Mapper<in P, out T> {
    fun toUI(param: P): T
}