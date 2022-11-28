package com.example.antly.sharedPreferences

import android.content.Context
import com.example.antly.sharedPreferences.data.ApiTokenSharedPreference
import com.example.antly.sharedPreferences.data.LoggedPasswordSharedPreference
import com.example.antly.sharedPreferences.data.LoggedUsernameSharedPreference
import javax.inject.Inject

class SharedPreferencesService @Inject constructor(
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

    fun getLoggedUsername(): String {
        return getSharedPreference(LoggedUsernameSharedPreference())
    }

    fun saveLoggedUsername(username: String) {
        val apiTokenSharedPreference = LoggedUsernameSharedPreference()
        apiTokenSharedPreference.value = username

        setSharedPreference(apiTokenSharedPreference)
    }

    fun getLoggedUserPassword(): String {
        return getSharedPreference(LoggedPasswordSharedPreference())
    }

    fun saveLoggedUserPassword(password: String) {
        val apiTokenSharedPreference = LoggedPasswordSharedPreference()
        apiTokenSharedPreference.value = password

        setSharedPreference(apiTokenSharedPreference)
    }
}