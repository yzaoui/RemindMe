package com.bitwiserain.remindme.presentation.viewmodel

import com.bitwiserain.remindme.CoroutineTest
import com.bitwiserain.remindme.InstantTaskExecutorExtension
import com.bitwiserain.remindme.ReminderTimeUnit
import com.bitwiserain.remindme.core.repository.ReminderRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
        viewModel = EditReminderDialogViewModel(testCoroutineDispatcher, stubRepo)
    }

    @Nested @DisplayName("Given an initial viewmodel")
    inner class Initial {
        @Test @DisplayName("When getting title, Then it should be empty")
        fun titleEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.title.value.shouldBeEmpty()
        }

        @Test @DisplayName("When getting time, Then it should be empty")
        fun timeEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.time.value.shouldBeEmpty()
        }

        @Test @DisplayName("When getting time unit, Then it should be HOURS")
        fun timeUnitEmpty() = testCoroutineScope.runBlockingTest {
            viewModel.selectedUnitPosition.value shouldBeExactly ReminderTimeUnit.HOURS.ordinal
        }

        @Test @DisplayName("When getting state, Then it should be EDITING")
        fun stateEditing() {
            viewModel.state.value shouldBe EditReminderDialogViewModel.State.Editing
        }

        @Test @DisplayName("When getting save enabled, Then it should be false")
        fun saveEnabledFalse() = testCoroutineScope.runBlockingTest {
            viewModel.saveEnabled.value.shouldBeFalse()
        }

        @Test @DisplayName("When attempting to discard, Then the state should be DISCARDED")
        fun initialDiscardSetsDiscarded() {
            viewModel.discard()

            viewModel.state.value shouldBe EditReminderDialogViewModel.State.Discarded
        }
    }

    @Nested @DisplayName("Given a fully filled reminder")
    inner class FullyFilledReminder {
        private val initialTitle = "Some reminder text"
        private val initialTime = "5"

        @BeforeEach
        fun beforeEach() {
            viewModel.title.value = initialTitle
            viewModel.time.value = initialTime
        }

        @Test @DisplayName("When getting save enabled, Then it should be true")
        fun saveEnabledTrue() = testCoroutineScope.runBlockingTest {
            viewModel.saveEnabled.value.shouldBeTrue()
        }

        @Nested @DisplayName("When attempting to discard")
        inner class AttemptingDiscard {
            @BeforeEach
            fun beforeEach() {
                viewModel.discard()
            }

            @Test @DisplayName("Then the state is CONFIRM_DISCARD")
            fun isConfirmDiscard() {
                viewModel.state.value shouldBe EditReminderDialogViewModel.State.ConfirmDiscard
            }

            @Test @DisplayName("Then discard should be confirmable and goes to DISCARDED state")
            fun discardConfirmable() {
                viewModel.confirmDiscard()

                viewModel.state.value shouldBe EditReminderDialogViewModel.State.Discarded
            }

            @Test @DisplayName("Then discard should be cancellable and returns to EDITING state with original inputs")
            fun discardCancellable() {
                viewModel.cancelDiscard()

                assertSoftly {
                    viewModel.state.value shouldBe EditReminderDialogViewModel.State.Editing
                    viewModel.title.value shouldBe initialTitle
                    viewModel.time.value shouldBe initialTime
                }
            }
        }
    }
}
