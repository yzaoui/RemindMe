package com.bitwiserain.remindme.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitwiserain.remindme.databinding.FragmentReminderListBinding
import com.bitwiserain.remindme.presentation.viewmodel.ReminderListViewModel
import com.bitwiserain.remindme.room.Reminder
import com.bitwiserain.remindme.util.InjectorUtils

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReminderListFragment.OnReminderItemInteractionListener] interface.
 */
class ReminderListFragment : Fragment() {
    private lateinit var binding: FragmentReminderListBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReminderListBinding.inflate(inflater, container, false)

        binding.reminderListRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ReminderItemRecyclerViewAdapter(
                deleteReminder = ::deleteReminder,
                lco =  this@ReminderListFragment,
                initialExpandedReminderId = if (args.scrollToReminderId != -1) args.scrollToReminderId else null,
                onInitialReminderExpanded = ::scrollToPosition
            )
        }

        binding.fab.setOnClickListener { listener?.onCreateReminderClick() }

        viewModel.reminders.observe(viewLifecycleOwner, { reminders ->
            (binding.reminderListRecycler.adapter as ReminderItemRecyclerViewAdapter).submitList(reminders)
        })

        return binding.root
    }

    private fun deleteReminder(reminder: Reminder) {
        viewModel.deleteReminder(reminder)
        listener?.onReminderDelete()
        NotificationManagerCompat.from(requireContext()).cancel(reminder.id)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
