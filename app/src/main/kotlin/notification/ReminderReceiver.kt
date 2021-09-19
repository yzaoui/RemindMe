package com.bitwiserain.remindme.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.bitwiserain.remindme.util.InjectorUtils
import com.bitwiserain.remindme.util.PACKAGE_PREFIX
import kotlinx.coroutines.runBlocking

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Action.REMINDER_ELAPSED.key -> {
                val reminder = runBlocking { InjectorUtils.provideReminderRepository(context).getReminder(intent.extras!!.getInt(Extra.ELAPSED_REMINDER_ID.key)) }

                NotificationHelper.createNotification(context, reminder)
            }
            Action.DELETE_REMINDER.key -> {
                val reminderId = intent.data!!.pathSegments[1].toInt()

                runBlocking { InjectorUtils.provideReminderRepository(context).deleteReminder(reminderId) }
                NotificationManagerCompat.from(context).cancel(reminderId)
            }
        }
    }

    enum class Action(val key: String) {
        REMINDER_ELAPSED(PACKAGE_PREFIX + "REMINDER_ELAPSED"),
        DELETE_REMINDER(PACKAGE_PREFIX + "DELETE_REMINDER")
    }

    enum class Extra(val key: String) {
        ELAPSED_REMINDER_ID(PACKAGE_PREFIX + "ELAPSED_REMINDER_ID")
    }
}
