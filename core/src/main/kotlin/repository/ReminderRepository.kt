package com.bitwiserain.remindme.core.repository

import com.bitwiserain.remindme.core.model.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface ReminderRepository {
    fun getReminders(): Flow<List<Reminder>>
    suspend fun getReminder(id: Int): Reminder
    suspend fun insertReminder(reminder: NewReminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteReminder(reminderId: Int)

    data class NewReminder(val title: String, val time: Instant)
}
