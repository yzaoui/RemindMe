package com.bitwiserain.remindme

import com.bitwiserain.remindme.domain.ReminderRepository
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModel
import com.bitwiserain.remindme.room.Reminder
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.threeten.bp.Instant

@ExtendWith(InstantTaskExecutorExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReminderListViewModelTests : CoroutineTest {
    override lateinit var testCoroutineScope: TestCoroutineScope
    override lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    private val initialFakeReminders: List<Reminder> = listOf(
        Reminder("0", Instant.ofEpochSecond(5000L)).apply { id = 0 },
        Reminder("1", Instant.ofEpochSecond(4000L)).apply { id = 1 },
        Reminder("2", Instant.ofEpochSecond(6000L)).apply { id = 2 }
    )

    private lateinit var fakeRepo: ReminderRepository

    private lateinit var viewModel: ReminderListViewModel

    @BeforeEach
    fun beforeAll() {
        fakeRepo = object : ReminderRepository {
            private val reminders: MutableStateFlow<List<Reminder>> = MutableStateFlow(initialFakeReminders)

            override fun getReminders(): Flow<List<Reminder>> = reminders
            override suspend fun getReminder(id: Int): Reminder = doNothing()
            override suspend fun insertReminder(reminder: NewReminder) = doNothing()
            override suspend fun deleteReminder(reminder: Reminder) { reminders.value -= reminder }
        }

        viewModel = ReminderListViewModel(repo = fakeRepo)
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

fun doNothing(): Nothing = throw NotImplementedError()
