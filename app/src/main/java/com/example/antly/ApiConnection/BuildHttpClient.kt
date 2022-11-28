package com.example.antly.ApiConnection

import com.example.antly.sharedPreferences.SharedPreferencesService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BuildHttpClient @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
) {

    fun buildHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        httpBuilder.addInterceptor { chain ->
            val urlPath = chain.request().url.encodedPath
            val url = chain.request().url
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
            var firstResponse = chain.proceed(request)



            if (firstResponse.code == 403 || firstResponse.code == 401) {
                firstResponse.close()
                val jsonObject = JSONObject()
                jsonObject.put("username", sharedPreferencesService.getLoggedUsername())
                jsonObject.put("password",  sharedPreferencesService.getLoggedUserPassword())

                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                var req =
                    request.newBuilder().url("https://${url.host}/auth/signin").post(body).build()
                val login = chain.proceed(req)
                val jsonObject2 = JSONObject(login.peekBody(Long.MAX_VALUE).string())

                sharedPreferencesService.saveApiToken(jsonObject2.get("accessToken").toString())
                login.close()
                val token = sharedPreferencesService.getApiToken()
                val renewedRequest = chain.request().newBuilder().addHeader(
                    "Authorization",
                    "Bearer $token"
                ).build()

                return@addInterceptor chain.proceed(renewedRequest)

            } else {
                return@addInterceptor firstResponse
            }
        }

        return httpBuilder.build()
    }
}