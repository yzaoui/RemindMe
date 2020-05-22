package com.bitwiserain.remindme

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import com.bitwiserain.remindme.databinding.EditReminderBinding
import org.threeten.bp.Instant

class EditReminderDialogFragment : AppCompatDialogFragment() {
    private lateinit var listener: OnReminderSaveListener
    private var _binding: EditReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EditReminderBinding.inflate(inflater, container, false)

        requireDialog().setTitle("Create/Edit reminder")

        binding.editReminderSpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.edit_reminder_time_units, android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.editReminderDiscard.setOnClickListener { onDiscardButtonClick() }
        binding.editReminderSave.setOnClickListener { onSaveButtonClick() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun onDiscardButtonClick() {
        dismiss()
    }

    private fun onSaveButtonClick() {
        val title = binding.editReminderTitle.text.toString()
        val time = Instant.now().plusSeconds(binding.editReminderTime.text.toString().toLong())

        listener.onReminderSave(NewReminder(title, time))
        dismiss()
    }

    interface OnReminderSaveListener {
        fun onReminderSave(reminder: NewReminder)
    }
}
