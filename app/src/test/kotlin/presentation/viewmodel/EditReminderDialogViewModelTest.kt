package com.bitwiserain.remindme.presentation.viewmodel

import com.bitwiserain.remindme.CoroutineTest
import com.bitwiserain.remindme.InstantTaskExecutorExtension
import com.bitwiserain.remindme.ReminderTimeUnit
import com.bitwiserain.remindme.domain.ReminderRepository
import com.bitwiserain.remindme.getOrAwaitValue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
internal class EditReminderDialogViewModelTest : CoroutineTest {
    override lateinit var testCoroutineScope: TestCoroutineScope
    override lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    private lateinit var stubRepo: ReminderRepository
    private lateinit var viewModel: EditReminderDialogViewModel

    @BeforeEach
    fun beforeAll() {
        stubRepo = StubReminderRepository()
        viewModel = EditReminderDialogViewModel(repo = stubRepo)
    }

    @Nested @DisplayName("Given an initial viewmodel")
    inner class Initial {
        @Test @DisplayName("When getting title, Then it should be empty")
        fun titleEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.title.getOrAwaitValue().shouldBeEmpty()
        }

        @Test @DisplayName("When getting time, Then it should be empty")
        fun timeEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.time.getOrAwaitValue().shouldBeEmpty()
        }

        @Test @DisplayName("When getting time unit, Then it should be HOURS")
        fun timeUnitEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.selectedUnitPosition.getOrAwaitValue() shouldBeExactly ReminderTimeUnit.HOURS.ordinal
        }

        @Test @DisplayName("When getting state, Then it should be EDITING")
        fun stateEditing() = testCoroutineScope.runBlockingTest {
            viewModel.state.value shouldBe EditReminderDialogViewModel.State.Editing
        }
    }
}
