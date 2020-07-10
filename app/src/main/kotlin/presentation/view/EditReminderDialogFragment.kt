package com.bitwiserain.remindme.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.databinding.EditReminderBinding
import com.bitwiserain.remindme.presentation.viewmodel.EditReminderDialogViewModel
import com.bitwiserain.remindme.presentation.viewmodel.EditReminderDialogViewModel.State.*
import com.bitwiserain.remindme.util.InjectorUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditReminderDialogFragment : AppCompatDialogFragment() {
    private lateinit var listener: OnReminderSaveListener
    private lateinit var binding: EditReminderBinding

    private val viewModel: EditReminderDialogViewModel by viewModels {
        InjectorUtils.provideEditReminderDialogViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = EditReminderBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@EditReminderDialogFragment.viewLifecycleOwner
            viewModel = this@EditReminderDialogFragment.viewModel
            editReminderSpinner.apply {
                adapter = ArrayAdapter.createFromResource(requireContext(), R.array.edit_reminder_time_units, android.R.layout.simple_spinner_item).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.state.collect { handleStateTransition(it) }
        }

        requireDialog().run {
            setTitle(getString(R.string.edit_reminder_title))
            // TODO: Would be nice to have this cancelable, investigate how to intercept cancel
            isCancelable = false
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnReminderSaveListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ${OnReminderSaveListener::class.qualifiedName}")
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogWithTitle
    }

    private fun handleStateTransition(state: EditReminderDialogViewModel.State) {
        when (state) {
            is Editing -> Unit
            is ConfirmDiscard -> confirmDiscard()
            is Discarded -> dismiss()
            is Submitted -> {
                listener.onReminderSave(state.newReminder)
                dismiss()
            }
        }
    }

    private fun confirmDiscard() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(getString(R.string.edit_reminder_discard_confirmation_message))
            setCancelable(true)
            setPositiveButton(getString(R.string.edit_reminder_discard_confirmation_message_positive)) { _, _ ->
                viewModel.discardConfirmed()
            }
            setNegativeButton(getString(R.string.edit_reminder_discard_confirmation_message_negative)) { dialog, _ ->
                dialog.cancel()
            }
            setOnCancelListener {
                viewModel.discardCancelled()
            }
        }.create().show()
    }

    interface OnReminderSaveListener {
        fun onReminderSave(reminder: NewReminder)
    }
}
