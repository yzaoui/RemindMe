package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.Reminder
import com.bitwiserain.remindme.ReminderRepository
import kotlinx.coroutines.launch

class ReminderListViewModel internal constructor(private val repo: ReminderRepository) : ViewModel() {
    val reminders: LiveData<List<Reminder>> = repo.getReminders()

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch {
        repo.deleteReminder(reminder)
    }
}
