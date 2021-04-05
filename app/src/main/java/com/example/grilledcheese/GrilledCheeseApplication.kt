package com.example.grilledcheese

import android.app.Application
import com.facebook.stetho.Stetho

class GrilledCheeseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}