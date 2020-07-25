package com.bitwiserain.remindme.presentation.viewmodel

import com.bitwiserain.remindme.CoroutineTest
import com.bitwiserain.remindme.InstantTaskExecutorExtension
import com.bitwiserain.remindme.domain.ReminderRepository
import com.bitwiserain.remindme.getOrAwaitValue
import com.bitwiserain.remindme.room.Reminder
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.threeten.bp.Instant

@ExtendWith(InstantTaskExecutorExtension::class)
internal class ReminderListViewModelTest : CoroutineTest {
    override lateinit var testCoroutineScope: TestCoroutineScope
    override lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    private val initialFakeReminders: List<Reminder> = listOf(
        Reminder("0", Instant.ofEpochSecond(5000L)).apply { id = 0 },
        Reminder("1", Instant.ofEpochSecond(4000L)).apply { id = 1 },
        Reminder("2", Instant.ofEpochSecond(6000L)).apply { id = 2 }
    )

    private lateinit var stubRepo: ReminderRepository

    private lateinit var viewModel: ReminderListViewModel

    @BeforeEach
    fun beforeAll() {
        stubRepo = StubReminderRepository(initialFakeReminders, initialFakeReminders.last().id)
        viewModel = ReminderListViewModel(repo = stubRepo)
    }

    @Nested @DisplayName("Given reminders in the database")
    inner class RemindersExist {
        @Nested @DisplayName("When getting the reminders")
        inner class OnGetReminders {
            lateinit var reminders: List<Reminder>

            @BeforeEach
            fun beforeEach() = testCoroutineScope.runBlockingTest {
                reminders = viewModel.reminders.getOrAwaitValue()
            }

            @Test @DisplayName("Then the same reminders are returned")
            fun remindersExist() {
                reminders shouldContainExactlyInAnyOrder initialFakeReminders
            }

            @Test @DisplayName("Then the reminders are returned in chronological order")
            fun remindersInChronologicalOrder() {
                reminders shouldBeSortedWith { r1, r2 -> r1.time.compareTo(r2.time) }
            }
        }

        @Test @DisplayName("When deleting a reminder, Then the deleted reminder should be removed")
        fun reminderShouldBeDeleted() {
            var reminders: List<Reminder>? = null
            val reminderToDelete = initialFakeReminders[1]

            testCoroutineScope.runBlockingTest {
                viewModel.deleteReminder(reminderToDelete)

                reminders = viewModel.reminders.getOrAwaitValue()
            }

            reminders shouldContainExactlyInAnyOrder initialFakeReminders.minus(reminderToDelete)
        }
    }
}
