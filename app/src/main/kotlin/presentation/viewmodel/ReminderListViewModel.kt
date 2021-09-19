package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.core.model.Reminder
import com.bitwiserain.remindme.core.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReminderListViewModel internal constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repo: ReminderRepository
) : ViewModel() {
    val reminders: LiveData<List<Reminder>> = repo.getReminders()
        .map { it.sortedBy { it.time } }
        .asLiveData()

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch(ioDispatcher) {
        repo.deleteReminder(reminder)
    }
}
