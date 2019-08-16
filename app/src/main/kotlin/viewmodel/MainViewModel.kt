package com.bitwiserain.remindme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.ReminderRepository
import kotlinx.coroutines.launch

class MainViewModel internal constructor(private val repo: ReminderRepository) : ViewModel() {
    fun insertReminder(reminder: NewReminder) = viewModelScope.launch {
        repo.insertReminder(reminder)
    }
}
