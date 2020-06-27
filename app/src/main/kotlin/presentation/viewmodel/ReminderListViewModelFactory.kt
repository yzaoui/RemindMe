package com.bitwiserain.remindme.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitwiserain.remindme.ReminderRepository

class ReminderListViewModelFactory(private val repo: ReminderRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ReminderListViewModel(repo) as T
}
