package com.bitwiserain.remindme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bitwiserain.remindme.util.InjectorUtils
import com.bitwiserain.remindme.util.PACKAGE_PREFIX
import kotlinx.coroutines.runBlocking

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.equals(Action.REMINDER_ELAPSED.key, ignoreCase = true) == true) {
            val reminder = runBlocking { InjectorUtils.provideReminderRepository(context).getReminder(intent.extras!!.getInt(Extra.ELAPSED_REMINDER_ID.key)) }

            NotificationHelper.createNotification(context, reminder)
        }
    }

    enum class Action(val key: String) {
        REMINDER_ELAPSED(PACKAGE_PREFIX + "REMINDER_ELAPSED")
    }

    enum class Extra(val key: String) {
        ELAPSED_REMINDER_ID(PACKAGE_PREFIX + "ELAPSED_REMINDER_ID")
    }
}
