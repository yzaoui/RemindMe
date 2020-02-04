package com.bitwiserain.remindme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_reminder_item.view.*
import org.threeten.bp.Duration
import org.threeten.bp.Instant

/**
 * [RecyclerView.Adapter] that can display a [Reminder] and makes a call to the
 * specified [ReminderListFragment.OnReminderItemInteractionListener].
 */
class ReminderItemRecyclerViewAdapter(
    private val listener: ReminderListFragment.OnReminderItemInteractionListener,
    private val lco: LifecycleOwner)
: ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Reminder
            listener.onReminderItemInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReminderViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reminder_item, parent, false)
    )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = getItem(position)
        holder.apply {
            bind(reminder, onClickListener)
            view.tag = reminder
        }

        with(Tick.timer) {
            removeObserver(holder)
            observe(lco, holder)
        }
    }
}

class ReminderViewHolder(val view: View) : RecyclerView.ViewHolder(view), Observer<Instant> {
    private val titleView: TextView = view.reminder_item_title
    private val timeView: TextView = view.reminder_item_time
    private var reminder: Reminder? = null

    fun bind(reminder: Reminder, listener: View.OnClickListener) {
        view.setOnClickListener(listener)
        titleView.text = reminder.title
        timeView.text = secondsToNow(reminder.time, Instant.now())
        this.reminder = reminder
    }

    private fun secondsToNow(time: Instant, now: Instant): String {
        return "${Duration.between(now, time).seconds}s"
    }

    override fun onChanged(now: Instant?) {
        val reminder = this.reminder ?: return

        if (now != null) {
            timeView.text = if (reminder.isElapsed(now)) "PASSED" else secondsToNow(reminder.time, now)
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
