package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.*
import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.ReminderTimeUnit
import com.bitwiserain.remindme.domain.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class EditReminderDialogViewModel internal constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repo: ReminderRepository
) : ViewModel() {
    val title = MutableLiveData<String>("")

    val time = MutableLiveData<String>("")

    val selectedUnitPosition = MutableLiveData<Int>(ReminderTimeUnit.HOURS.ordinal)

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Editing)
    val state: StateFlow<State>
        get() = _state

    @ExperimentalContracts
    private val _saveEnabled = MediatorLiveData<Boolean>().apply {
        addSource(title) { updateSaveEnabled() }
        addSource(time) { updateSaveEnabled() }
    }
    @ExperimentalContracts
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    @ExperimentalContracts
    private fun updateSaveEnabled() {
        // Save is only available if fields are valid
        _saveEnabled.value = title.value.isValidTitle() && time.value.isValidTime()
    }

    @ExperimentalContracts
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

            _state.value = State.Submitted(newReminder)
        }
    }

    fun discard() {
        if (!title.value.isNullOrBlank() || !time.value.isNullOrBlank()) {
            _state.value = State.ConfirmDiscard
        } else {
            _state.value = State.Discarded
        }
    }

    fun discardCancelled() {
        if (_state.value == State.ConfirmDiscard) _state.value = State.Editing
    }

    fun discardConfirmed() {
        if (_state.value == State.ConfirmDiscard) _state.value = State.Discarded
    }

    sealed class State {
        object Editing : State()
        object ConfirmDiscard : State()
        object Discarded : State()
        data class Submitted(
            val newReminder: NewReminder
        ) : State()
    }
}

@ExperimentalContracts
private fun String?.isValidTitle(): Boolean {
    contract {
        returns(true) implies (this@isValidTitle != null)
    }

    return !isNullOrBlank()
}

@ExperimentalContracts
private fun String?.isValidTime(): Boolean {
    contract {
        returns(true) implies (this@isValidTime != null)
    }

    return this?.toLongOrNull()?.let { it >= 0 } ?: false
}

@ExperimentalContracts
private fun Int?.isValidUnitPosition(): Boolean {
    contract {
        returns(true) implies (this@isValidUnitPosition != null)
    }

    return this != null && this in ReminderTimeUnit.values().indices
}
