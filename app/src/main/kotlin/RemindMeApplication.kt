package com.bitwiserain.remindme

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class RemindMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Required to initialize timezone information
        AndroidThreeTen.init(this)
    }
}
