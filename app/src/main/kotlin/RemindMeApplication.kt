package com.bitwiserain.remindme

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.bitwiserain.remindme.notification.NotificationHelper
import com.bitwiserain.remindme.notification.ReminderScheduler

class RemindMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ReminderScheduler.init(applicationContext)

        NotificationHelper.createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false, getString(R.string.app_name), "App notification channel.")
    }
}
