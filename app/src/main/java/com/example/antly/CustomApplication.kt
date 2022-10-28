package com.example.antly

import android.app.Application
import com.example.antly.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomApplication : Application() {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
        context = this

        // Start Koin
        startKoin {
            androidContext(this@CustomApplication)
            modules(applicationModule)
        }

        // Show leaked resources if application in dev mode
        if(BuildConfig.DEBUG){
//            StrictMode.enableDefaults();
        }
    }

    companion object {
        var context: CustomApplication? = null
            private set
    }
}