package com.bitwiserain.remindme

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModel
import com.bitwiserain.remindme.room.AppDatabase
import com.bitwiserain.remindme.room.Reminder
import com.bitwiserain.remindme.room.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.threeten.bp.Instant

@RunWith(RobolectricTestRunner::class)
class ReminderListViewModelTests {
    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: ReminderListViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val reminderRepo = ReminderRepository.getInstance(appDatabase.reminderDAO()).apply {
            runBlocking {
                insertReminder(NewReminder("0", Instant.ofEpochSecond(5000L)))
                insertReminder(NewReminder("1", Instant.ofEpochSecond(4000L)))
                insertReminder(NewReminder("2", Instant.ofEpochSecond(6000L)))
            }
        }

        viewModel = ReminderListViewModel(reminderRepo)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun `Given reminders in the db, when getting them, they should all return`() {
        val reminders: List<Reminder> = runBlocking {
            withContext(Dispatchers.IO) {
                viewModel.reminders.getOrAwaitValue()
            }
        }

        assertEquals(3, reminders.size)
    }
}
