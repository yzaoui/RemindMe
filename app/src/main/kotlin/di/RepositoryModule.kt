package com.bitwiserain.remindme.di

import com.bitwiserain.remindme.domain.ReminderRepository

object RepositoryModule {
    fun provideReminderRepository(): ReminderRepository
}
