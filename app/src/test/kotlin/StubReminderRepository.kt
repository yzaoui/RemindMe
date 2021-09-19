package com.bitwiserain.remindme.presentation.viewmodel

import com.bitwiserain.remindme.core.repository.ReminderRepository
import com.bitwiserain.remindme.core.repository.ReminderRepository.NewReminder
import com.bitwiserain.remindme.room.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import com.bitwiserain.remindme.core.model.Reminder as DomainReminder

class StubReminderRepository(initialReminders: List<Reminder> = emptyList(), private var lastId: Int = 0) : ReminderRepository {
    private val reminders: MutableStateFlow<List<DomainReminder>> = MutableStateFlow(initialReminders)

    override fun getReminders() = reminders

    override suspend fun getReminder(id: Int): Reminder = doNothing()

    override suspend fun insertReminder(reminder: NewReminder) {
        reminders.value += Reminder(reminder.title, reminder.time).apply {
            id = lastId++
        }
    }

    override suspend fun deleteReminder(reminder: DomainReminder) {
        reminders.value -= reminder
    }

    override suspend fun deleteReminder(reminderId: Int) {
        TODO("Not yet implemented")
    }
}

fun doNothing(): Nothing = throw NotImplementedError()
