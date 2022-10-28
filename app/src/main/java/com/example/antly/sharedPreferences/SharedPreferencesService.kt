package com.example.antly.sharedPreferences

import android.content.Context
import com.example.antly.sharedPreferences.data.ApiTokenSharedPreference

class SharedPreferencesService(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "SHARED_PREFERENCES",
        Context.MODE_PRIVATE
    )

    private fun getSharedPreference(sharedPreference: SharedPreference): String {
        val string = sharedPreferences.getString(sharedPreference.getName(), "")
        return string ?: ""
    }

    private fun setSharedPreference(sharedPreference: SharedPreference) {
        sharedPreferences.edit()
            .putString(sharedPreference.getName(), sharedPreference.value)
            .apply()
    }

    fun getApiToken(): String {
        return getSharedPreference(ApiTokenSharedPreference())
    }

    fun saveApiToken(apiToken: String) {
        val apiTokenSharedPreference = ApiTokenSharedPreference()
        apiTokenSharedPreference.value = apiToken

        setSharedPreference(apiTokenSharedPreference)
    }
}