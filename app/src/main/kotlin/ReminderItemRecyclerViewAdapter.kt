package com.bitwiserain.remindme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_reminder_item.view.*
import org.threeten.bp.Duration
import org.threeten.bp.Instant

class ReminderItemRecyclerViewAdapter(
    private val deleteReminder: (reminder: Reminder) -> Unit,
    private val lco: LifecycleOwner
) : ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
    private val onClickExpandListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Reminder
        expandedReminderId.postValue(item.id)
    }
    private var expandedReminderId: MutableLiveData<Int> = MutableLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReminderViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.view_reminder_item, parent, false),
        expandReminderListener = onClickExpandListener,
        deleteReminder = deleteReminder
    )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = getItem(position)

        holder.bind(reminder)

        with(Tick.timer) {
            removeObserver(holder.tickObserver)
            observe(lco, holder.tickObserver)
        }

        with(expandedReminderId) {
            removeObserver(holder.expandedObserver)
            observe(lco, holder.expandedObserver)
        }
    }
}

class ReminderViewHolder(val view: View, expandReminderListener: View.OnClickListener, deleteReminder: (reminder: Reminder) -> Unit) : RecyclerView.ViewHolder(view) {
    private val titleView: TextView = view.reminder_item_title
    private val timeView: TextView = view.reminder_item_time
    private val deleteButton: Button = view.reminder_delete.apply {
        setOnClickListener { this@ReminderViewHolder.reminder?.let(deleteReminder) }
    }
    private var reminder: Reminder? = null
    val tickObserver = Observer<Instant> { now: Instant? ->
        val reminder = this@ReminderViewHolder.reminder ?: return@Observer

        if (now != null) {
            timeView.text = if (reminder.isElapsed(now)) "ELAPSED" else secondsToNow(reminder.time, now)
        }
    }
    val expandedObserver = Observer<Int> { reminderId: Int? ->
        deleteButton.visibility = if (reminderId == this.reminder?.id) View.VISIBLE else View.GONE
    }

    init {
        view.setOnClickListener(expandReminderListener)
    }

    fun bind(reminder: Reminder) {
        view.tag = reminder
        titleView.text = reminder.title
        timeView.text = secondsToNow(reminder.time, Instant.now())
        this.reminder = reminder
    }

    private fun secondsToNow(time: Instant, now: Instant): String {
        return "${Duration.between(now, time).seconds}s"
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
