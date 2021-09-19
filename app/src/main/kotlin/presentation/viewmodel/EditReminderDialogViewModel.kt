package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwiserain.remindme.ReminderTimeUnit
import com.bitwiserain.remindme.core.repository.ReminderRepository
import com.bitwiserain.remindme.core.repository.ReminderRepository.NewReminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class EditReminderDialogViewModel internal constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repo: ReminderRepository
) : ViewModel() {
    val title: MutableStateFlow<String> = MutableStateFlow("")

    val time: MutableStateFlow<String> = MutableStateFlow("")

    val selectedUnitPosition: MutableStateFlow<Int> = MutableStateFlow(ReminderTimeUnit.HOURS.ordinal)

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Editing)
    val state: StateFlow<State> = _state

    val saveEnabled: StateFlow<Boolean> = combine(title, time) { title, time ->
        title.isValidTitle() && time.isValidTime()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun saveReminder() {
        val currentTitle = title.value
        val currentTime = time.value
        val currentSelectedUnitPosition = selectedUnitPosition.value

        if (currentTitle.isValidTitle() && currentTime.isValidTime() && currentSelectedUnitPosition.isValidUnitPosition()) {
            val newTitle = currentTitle.trim()
            val newTime = Instant.now().plus(
                currentTime.toLong(),
                when (ReminderTimeUnit.values()[currentSelectedUnitPosition]) {
                    ReminderTimeUnit.SECONDS -> ChronoUnit.SECONDS
                    ReminderTimeUnit.MINUTES -> ChronoUnit.MINUTES
                    ReminderTimeUnit.HOURS -> ChronoUnit.HOURS
                }
            )

            val newReminder = NewReminder(newTitle, newTime)

            // Save reminder to database
            viewModelScope.launch(ioDispatcher) { repo.insertReminder(newReminder) }

            _state.value = State.Submitted
        }
    }

    fun discard() {
        if (title.value.isNotBlank() || time.value.isNotBlank()) {
            _state.value = State.ConfirmDiscard
        } else {
            _state.value = State.Discarded
        }
    }

    fun cancelDiscard() {
        if (_state.value == State.ConfirmDiscard) _state.value = State.Editing
    }

    fun confirmDiscard() {
        if (_state.value == State.ConfirmDiscard) _state.value = State.Discarded
    }

    sealed class State {
        object Editing : State()
        object ConfirmDiscard : State()
        object Discarded : State()
        object Submitted : State()
    }
}

private fun String.isValidTitle() = isNotBlank()

private fun String.isValidTime() = toLongOrNull()?.let { it >= 0 } ?: false

private fun Int.isValidUnitPosition() = this in ReminderTimeUnit.values().indices
