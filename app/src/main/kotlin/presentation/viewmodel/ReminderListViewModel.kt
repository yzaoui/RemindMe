package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.core.model.Reminder
import com.bitwiserain.remindme.core.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReminderListViewModel internal constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repo: ReminderRepository
) : ViewModel() {
    val reminders: StateFlow<List<Reminder>> = repo.getReminders()
        .map { it.sortedBy(Reminder::time) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch(ioDispatcher) {
        repo.deleteReminder(reminder)
    }
}
