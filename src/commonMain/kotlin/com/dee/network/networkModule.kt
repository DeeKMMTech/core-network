package com.dee.network

import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Created by Hein Htet
 */
val networkModule = module  {
    single<HttpClient> { provideHttpClient() }
}