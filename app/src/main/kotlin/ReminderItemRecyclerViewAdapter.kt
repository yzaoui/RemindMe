package com.bitwiserain.remindme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_reminder_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Reminder] and makes a call to the
 * specified [ReminderListFragment.OnReminderItemInteractionListener].
 */
class ReminderItemRecyclerViewAdapter(private val listener: ReminderListFragment.OnReminderItemInteractionListener?) : ListAdapter<Reminder, ReminderItemRecyclerViewAdapter.ViewHolder>(ReminderDiffCallback()) {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Reminder
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener?.onReminderItemInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reminder_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = getItem(position)

        holder.titleView.text = reminder.title
        holder.timeView.text = reminder.time

        with(holder.view) {
            tag = reminder
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.reminder_item_title
        val timeView: TextView = view.reminder_item_time

        override fun toString(): String {
            return super.toString() + " '" + timeView.text + "'"
        }
    }
}

private class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.title == newItem.title && oldItem.time == newItem.time
    }
}
