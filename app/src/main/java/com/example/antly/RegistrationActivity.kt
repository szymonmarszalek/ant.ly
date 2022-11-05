package com.example.antly

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    override fun onStart() {
        super.onStart()
        isUserLogged()
    }

    private fun isUserLogged() {
        println(sharedPreferencesService.getApiToken()+ "TOKEN")
        if (sharedPreferencesService.getApiToken().isNotEmpty()) {
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            startActivity(intent)
        }
    }
}