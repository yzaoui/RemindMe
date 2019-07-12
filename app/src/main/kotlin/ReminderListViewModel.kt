package com.bitwiserain.remindme

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReminderListViewModel : ViewModel() {
    val reminders: MutableLiveData<List<Reminder>> by lazy {
        MutableLiveData<List<Reminder>>()
    }

    init {
        reminders.value = listOf(
            Reminder(Reminder.newId(), "Respond to that important email", "2m37"),
            Reminder(Reminder.newId(), "Transfer tuition fee", "5h"),
            Reminder(Reminder.newId(), "Ask X about a possible extension", "1d2h")
        )
    }
}
