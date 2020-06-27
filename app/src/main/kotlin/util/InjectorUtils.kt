package com.bitwiserain.remindme.util

import android.content.Context
import com.bitwiserain.remindme.AppDatabase
import com.bitwiserain.remindme.ReminderRepository
import com.bitwiserain.remindme.presentation.viewmodel.EditReminderDialogViewModelFactory
import com.bitwiserain.remindme.presentation.viewmodel.MainViewModelFactory
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModelFactory

object InjectorUtils {
    fun provideReminderListViewModelFactory(context: Context) = ReminderListViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideMainViewModelFactory(context: Context) = MainViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideEditReminderDialogViewModelFactory(context: Context) = EditReminderDialogViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideReminderRepository(context: Context) = ReminderRepository.getInstance(
        AppDatabase.getInstance(context).reminderDAO()
    )
}
