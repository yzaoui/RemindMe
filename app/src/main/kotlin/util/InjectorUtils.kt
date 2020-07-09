package com.bitwiserain.remindme.util

import android.content.Context
import com.bitwiserain.remindme.room.AppDatabase
import com.bitwiserain.remindme.room.ReminderRepository
import com.bitwiserain.remindme.presentation.viewmodel.EditReminderDialogViewModelFactory
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModelFactory

object InjectorUtils {
    fun provideReminderListViewModelFactory(context: Context) = ReminderListViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideEditReminderDialogViewModelFactory(context: Context) = EditReminderDialogViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideReminderRepository(context: Context) = ReminderRepository.getInstance(
        AppDatabase.getInstance(context).reminderDAO()
    )
}
