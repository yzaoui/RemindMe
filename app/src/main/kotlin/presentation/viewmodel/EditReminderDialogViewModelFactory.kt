package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitwiserain.remindme.core.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers

class EditReminderDialogViewModelFactory(private val repo: ReminderRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = EditReminderDialogViewModel(Dispatchers.IO, repo) as T
}
