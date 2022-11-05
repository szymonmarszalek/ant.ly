package com.example.antly.ApiConnection

import com.example.antly.sharedPreferences.SharedPreferencesService
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BuildHttpClient @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService
    ) {

     fun buildHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        httpBuilder.addInterceptor { chain ->
            val urlPath = chain.request().url.encodedPath

            val request: Request
            if (urlPath != "/authentication") {
                val token = sharedPreferencesService.getApiToken()
                request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    "Bearer $token"
                ).build()

            } else {
                request = chain.request()
            }
            chain.proceed(request)
        }

        return httpBuilder.build()
    }
}