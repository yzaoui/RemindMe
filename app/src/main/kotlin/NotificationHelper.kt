package com.bitwiserain.remindme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getSystemService(NotificationManager::class.java).run {
                createNotificationChannel(
                    NotificationChannel(
                        "${context.packageName}-$name", name, importance).also {
                        it.description = description
                        it.setShowBadge(showBadge)
                    }
                )
            }
        }
    }

    fun createNotification(context: Context, reminder: Reminder) {
        // Unique channel ID for this app using package name & app name
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        NotificationCompat.Builder(context, channelId).apply {
            // Notification shade icon
            setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            setContentTitle(reminder.title)
            setContentText("It's time!")
            priority = NotificationCompat.PRIORITY_DEFAULT

            val pendingIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.let {
                PendingIntent.getActivity(context, 0, it, 0)
            }

            setContentIntent(pendingIntent)
        }.let {
            NotificationManagerCompat.from(context).notify(reminder.id, it.build())
        }
    }
}
