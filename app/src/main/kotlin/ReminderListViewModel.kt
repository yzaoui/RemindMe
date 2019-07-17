package com.bitwiserain.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class ReminderListViewModel : ViewModel() {
    val reminders = MutableLiveData<List<Reminder>>()

    private val _timer: MutableLiveData<Instant> = MutableLiveData()
    val timer: LiveData<Instant>
        get() = _timer

    init {
        Timer().apply {
            val now = Instant.now()
            val delay = 1000L - Duration.between(now.truncatedTo(ChronoUnit.SECONDS), now).toMillis()

            scheduleAtFixedRate(delay, 1000L) {
                _timer.postValue(Instant.now().truncatedTo(ChronoUnit.SECONDS))
            }
        }

        reminders.value = listOf(
            Reminder(Reminder.newId(), "Respond to that important email", instant(20L)),
            Reminder(Reminder.newId(), "Transfer tuition fee", instant(100L)),
            Reminder(Reminder.newId(), "Ask X about a possible extension", instant(180L)),
            Reminder(Reminder.newId(), "DDDDD", instant(50L)),
            Reminder(Reminder.newId(), "EEEEE", instant(25L)),
            Reminder(Reminder.newId(), "FFFFF", instant(150L)),
            Reminder(Reminder.newId(), "GGGGG", instant(66L)),
            Reminder(Reminder.newId(), "HHHHH", instant(48L)),
            Reminder(Reminder.newId(), "IIIII", instant(80L)),
            Reminder(Reminder.newId(), "JJJJJ", instant(90L)),
            Reminder(Reminder.newId(), "KKKKK", instant(94L)),
            Reminder(Reminder.newId(), "LLLLL", instant(71L)),
            Reminder(Reminder.newId(), "MMMMM", instant(68L))
        ).sortedBy { it.time }
    }

    private fun instant(seconds: Long) = Instant.now().plusSeconds(seconds).truncatedTo(ChronoUnit.SECONDS)
}
