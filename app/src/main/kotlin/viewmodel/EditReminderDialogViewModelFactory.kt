package com.bitwiserain.remindme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitwiserain.remindme.ReminderRepository

class EditReminderDialogViewModelFactory(private val repo: ReminderRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = EditReminderDialogViewModel(repo) as T
}