package com.bitwiserain.remindme

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.Instant

class ReminderListViewModel : ViewModel() {
    val reminders: MutableLiveData<List<Reminder>> by lazy {
        MutableLiveData<List<Reminder>>()
    }

    init {
        reminders.value = listOf(
            Reminder(Reminder.newId(), "Respond to that important email", Instant.now().plusSeconds(50L)),
            Reminder(Reminder.newId(), "Transfer tuition fee", Instant.now().plusSeconds(100L)),
            Reminder(Reminder.newId(), "Ask X about a possible extension", Instant.now().plusSeconds(150L))
        )
    }
}
