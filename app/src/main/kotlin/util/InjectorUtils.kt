package com.bitwiserain.remindme.util

import android.content.Context
import com.bitwiserain.remindme.core.repository.ReminderRepository
import com.bitwiserain.remindme.presentation.viewmodel.EditReminderDialogViewModelFactory
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModelFactory
import com.bitwiserain.remindme.room.AppDatabase
import com.bitwiserain.remindme.room.RoomReminderRepository
import kotlinx.coroutines.Dispatchers

object InjectorUtils {
    fun provideReminderListViewModelFactory(context: Context) = ReminderListViewModelFactory(
        ioDispatcher = Dispatchers.IO,
        repo = provideReminderRepository(context)
    )

    fun provideEditReminderDialogViewModelFactory(context: Context) = EditReminderDialogViewModelFactory(
        repo = provideReminderRepository(context)
    )

    fun provideReminderRepository(context: Context): ReminderRepository = RoomReminderRepository.getInstance(
        AppDatabase.getInstance(context).reminderDAO()
    )
}
