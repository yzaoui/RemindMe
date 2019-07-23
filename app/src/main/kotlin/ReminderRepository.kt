package com.bitwiserain.remindme

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class ReminderRepository private constructor(private val reminderDAO: ReminderDAO) {
    fun getReminders(): LiveData<List<Reminder>> = reminderDAO.getReminders()

    suspend fun insertReminder(reminder: NewReminder) = withContext(IO) {
        reminderDAO.insertReminder(Reminder(reminder.title, reminder.time))
    }

    companion object {
        @Volatile
        private var instance: ReminderRepository? = null

        fun getInstance(reminderDAO: ReminderDAO): ReminderRepository {
            return instance ?: synchronized(this) {
                instance ?: ReminderRepository(reminderDAO).also { instance = it }
            }
        }
    }
}
