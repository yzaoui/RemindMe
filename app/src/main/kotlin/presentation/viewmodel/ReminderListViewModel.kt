package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.room.Reminder
import com.bitwiserain.remindme.room.ReminderRepository
import kotlinx.coroutines.launch

class ReminderListViewModel internal constructor(private val repo: ReminderRepository) : ViewModel() {
    val reminders: LiveData<List<Reminder>> = Transformations.map(repo.getReminders()) {
        it.sortedBy { it.time }
    }

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch {
        repo.deleteReminder(reminder)
    }
}
