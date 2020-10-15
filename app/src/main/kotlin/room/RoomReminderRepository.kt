package com.bitwiserain.remindme.room

import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.domain.ReminderRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class RoomReminderRepository private constructor(private val reminderDAO: ReminderDAO) : ReminderRepository {
    override fun getReminders() = reminderDAO.getReminders()

    override suspend fun getReminder(id: Int) = reminderDAO.getReminder(id)

    override suspend fun insertReminder(reminder: NewReminder) = withContext(IO) {
        reminderDAO.insertReminder(Reminder(reminder.title, reminder.time))
    }

    override suspend fun deleteReminder(reminder: Reminder) = reminderDAO.deleteReminder(reminder)

    override suspend fun deleteReminderById(reminderId: Int) = reminderDAO.deleteReminderById(reminderId)

    companion object {
        @Volatile
        private var instance: RoomReminderRepository? = null

        fun getInstance(reminderDAO: ReminderDAO): RoomReminderRepository {
            return instance ?: synchronized(this) {
                instance ?: RoomReminderRepository(
                    reminderDAO
                ).also { instance = it }
            }
        }
    }
}
