package com.bitwiserain.remindme.domain

import com.bitwiserain.remindme.NewReminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getReminders(): Flow<List<Reminder>>
    suspend fun getReminder(id: Int): Reminder
    suspend fun insertReminder(reminder: NewReminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteReminderById(reminderId: Int)
}
