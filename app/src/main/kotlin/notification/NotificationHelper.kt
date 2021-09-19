package com.bitwiserain.remindme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.core.model.Reminder
import com.bitwiserain.remindme.presentation.view.MainActivity

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
            setContentTitle("Reminder:")
            setContentText(reminder.title)
            setContentIntent(
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = MainActivity.Action.GO_TO_REMINDER.key
                    data = Uri.Builder().apply {
                        appendPath("reminder")
                        appendPath(reminder.id.toString())
                    }.build()
                }.let {
                    PendingIntent.getActivity(context, 0, it, 0)
                }
            )
            addAction(android.R.drawable.ic_menu_delete, "Delete",
                Intent(context, ReminderReceiver::class.java).apply {
                    action = ReminderReceiver.Action.DELETE_REMINDER.key
                    data = Uri.Builder().apply {
                        appendPath("reminder")
                        appendPath(reminder.id.toString())
                    }.build()
                }.let {
                    PendingIntent.getBroadcast(context, 0, it, 0)
                }
            )

            priority = NotificationCompat.PRIORITY_DEFAULT
        }.let {
            NotificationManagerCompat.from(context).notify(reminder.id, it.build())
        }
    }
}
