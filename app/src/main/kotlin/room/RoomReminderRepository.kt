package com.bitwiserain.remindme.room

import com.bitwiserain.remindme.core.repository.ReminderRepository
import com.bitwiserain.remindme.core.repository.ReminderRepository.NewReminder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.bitwiserain.remindme.core.model.Reminder as DomainReminder

class RoomReminderRepository private constructor(private val reminderDAO: ReminderDAO) : ReminderRepository {
    override fun getReminders() = reminderDAO.getReminders().map {
        it.map { it.toDomain() }
    }

    override suspend fun getReminder(id: Int) = reminderDAO.getReminder(id).toDomain()

    override suspend fun insertReminder(reminder: NewReminder) = withContext(IO) {
        reminderDAO.insertReminder(Reminder(reminder.title, reminder.time))
    }

    override suspend fun deleteReminder(reminder: DomainReminder) = reminderDAO.deleteReminderById(reminder.id)

    override suspend fun deleteReminder(reminderId: Int) = reminderDAO.deleteReminderById(reminderId)

    private fun Reminder.toDomain() = DomainReminder(
        id = id,
        title = title,
        time = time
    )

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
