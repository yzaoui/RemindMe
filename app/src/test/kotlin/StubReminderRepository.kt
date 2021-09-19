package com.bitwiserain.remindme.presentation.viewmodel

import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.domain.ReminderRepository
import com.bitwiserain.remindme.room.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StubReminderRepository(initialReminders: List<Reminder> = emptyList(), private var lastId: Int = 0) : ReminderRepository {
    private val reminders: MutableStateFlow<List<Reminder>> = MutableStateFlow(initialReminders)

    override fun getReminders(): Flow<List<Reminder>> = reminders

    override suspend fun getReminder(id: Int): Reminder = doNothing()

    override suspend fun insertReminder(reminder: NewReminder) {
        reminders.value += Reminder(reminder.title, reminder.time).apply {
            id = lastId++
        }
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminders.value -= reminder
    }

    override suspend fun deleteReminderById(reminderId: Int) {
        TODO("Not yet implemented")
    }
}

fun doNothing(): Nothing = throw NotImplementedError()
