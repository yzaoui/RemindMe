package com.bitwiserain.remindme.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.asLiveData
import com.bitwiserain.remindme.domain.Reminder
import com.bitwiserain.remindme.domain.ReminderRepository
import com.bitwiserain.remindme.util.InjectorUtils
import java.time.Instant

object ReminderScheduler {
    private lateinit var alarmManager: AlarmManager
    private lateinit var repo: ReminderRepository

    fun init(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        repo = InjectorUtils.provideReminderRepository(context)

        repo.getReminders().asLiveData().observeForever {
            updateReminders(context, it)
        }
    }

    fun cancelNotification(context: Context, reminder: Reminder) {
        alarmManager.cancel(createPendingIntent(context, reminder))
    }

    private fun updateReminders(context: Context, reminders: List<Reminder>) {
        val now = Instant.now()
        val nextReminder = reminders.filter { !it.isElapsed(now) }.maxByOrNull(Reminder::id)

        if (nextReminder != null) {
            schedule(context, nextReminder)
        }
    }

    private fun schedule(context: Context, reminder: Reminder) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminder.time.toEpochMilli(),
            createPendingIntent(context, reminder)
        )
    }

    private fun createPendingIntent(context: Context, reminder: Reminder): PendingIntent? = Intent(
        context.applicationContext,
        ReminderReceiver::class.java
    ).apply {
        action = ReminderReceiver.Action.REMINDER_ELAPSED.key
        putExtra(ReminderReceiver.Extra.ELAPSED_REMINDER_ID.key, reminder.id)
    }.let {
        PendingIntent.getBroadcast(context, /*TODO: REQUESTCODE*/0, it, PendingIntent.FLAG_ONE_SHOT)
    }
}
