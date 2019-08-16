package com.bitwiserain.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReminderListViewModel internal constructor(private val repo: ReminderRepository) : ViewModel() {
    val reminders: LiveData<List<Reminder>> = repo.getReminders()

    fun insertReminder(reminder: NewReminder) {
        viewModelScope.launch {
            repo.insertReminder(reminder)
        }
    }
}
