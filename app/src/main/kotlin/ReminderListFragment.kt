package com.bitwiserain.remindme

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitwiserain.remindme.util.InjectorUtils
import com.bitwiserain.remindme.viewmodel.ReminderListViewModel
import kotlinx.android.synthetic.main.fragment_reminder_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReminderListFragment.OnReminderItemInteractionListener] interface.
 */
class ReminderListFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_reminder_list, container, false) as FrameLayout

        view.reminder_list_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ReminderItemRecyclerViewAdapter({ listener?.onReminderDelete(it) }, this@ReminderListFragment)
        }

        view.fab.setOnClickListener { listener?.onCreateReminderClick() }

        viewModel.reminders.observe(viewLifecycleOwner, Observer { reminders ->
            (view.reminder_list_recycler.adapter as ReminderItemRecyclerViewAdapter).submitList(reminders)
        })

        return view
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
        fun onReminderDelete(reminder: Reminder)
    }
}
