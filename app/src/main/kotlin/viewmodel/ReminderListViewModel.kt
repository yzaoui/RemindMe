package com.bitwiserain.remindme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bitwiserain.remindme.Reminder
import com.bitwiserain.remindme.ReminderRepository

class ReminderListViewModel internal constructor(repo: ReminderRepository) : ViewModel() {
    val reminders: LiveData<List<Reminder>> = repo.getReminders()
}
