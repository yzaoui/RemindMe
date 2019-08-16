package com.bitwiserain.remindme.util

import android.content.Context
import com.bitwiserain.remindme.AppDatabase
import com.bitwiserain.remindme.ReminderRepository
import com.bitwiserain.remindme.viewmodel.MainViewModelFactory
import com.bitwiserain.remindme.viewmodel.ReminderListViewModelFactory

object InjectorUtils {
    fun provideReminderListViewModelFactory(context: Context) = ReminderListViewModelFactory(
        repo = getReminderRepository(context)
    )

    fun provideMainViewModelFactory(context: Context) = MainViewModelFactory(
        repo = getReminderRepository(context)
    )

    private fun getReminderRepository(context: Context) = ReminderRepository.getInstance(
        AppDatabase.getInstance(context).reminderDAO()
    )
}
