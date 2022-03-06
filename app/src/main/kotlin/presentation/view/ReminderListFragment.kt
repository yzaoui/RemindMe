package com.bitwiserain.remindme.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.core.model.Reminder
import com.bitwiserain.remindme.databinding.FragmentReminderListBinding
import com.bitwiserain.remindme.notification.ReminderScheduler
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModel
import com.bitwiserain.remindme.util.InjectorUtils

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReminderListFragment.OnReminderItemInteractionListener] interface.
 */
class ReminderListFragment : Fragment() {
    private var _binding: FragmentReminderListBinding? = null
    private val binding get() = _binding!!
    private val args: ReminderListFragmentArgs by navArgs()
    private var listener: OnReminderItemInteractionListener? = null
    private val viewModel: ReminderListViewModel by viewModels {
        InjectorUtils.provideReminderListViewModelFactory(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is OnReminderItemInteractionListener) throw RuntimeException("$context must implement OnListFragmentInteractionListener")

        listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReminderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val elapsedString = binding.root.resources.getString(R.string.reminder_elapsed)

        binding.reminderList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val reminders = viewModel.reminders.collectAsState()

                if (reminders.value.isEmpty()) return@setContent

                ReminderListView(
                    reminders = reminders.value,
                    initiallyExpandedReminderId = if (args.scrollToReminderId != -1) args.scrollToReminderId else null,
                    elapsedString = elapsedString,
                    onDeleteReminder = ::deleteReminder
                )
            }
        }

        binding.fab.setOnClickListener { listener?.onCreateReminderClick() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun deleteReminder(reminder: Reminder) {
        viewModel.deleteReminder(reminder)
        listener?.onReminderDelete()
        // Cancel shown notification
        NotificationManagerCompat.from(requireContext()).cancel(reminder.id)
        // Cancel pending notification
        ReminderScheduler.cancelNotification(requireContext(), reminder)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnReminderItemInteractionListener {
        fun onCreateReminderClick()
        fun onReminderDelete()
    }
}
