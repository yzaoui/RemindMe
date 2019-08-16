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
import org.threeten.bp.temporal.ChronoUnit

/**
 * [RecyclerView.Adapter] that can display a [Reminder] and makes a call to the
 * specified [ReminderListFragment.OnReminderItemInteractionListener].
 */
class ReminderItemRecyclerViewAdapter(private val listener: ReminderListFragment.OnReminderItemInteractionListener, private val vm: ReminderListViewModel, private val lco: LifecycleOwner) : ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
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
        holder.bind(reminder)

        with(Tick.timer) {
            removeObserver(holder)
            observe(lco, holder)
        }

        with(holder.view) {
            tag = reminder
            setOnClickListener(onClickListener)
        }
    }
}

class ReminderViewHolder(val view: View) : RecyclerView.ViewHolder(view), Observer<Instant> {
    private val titleView: TextView = view.reminder_item_title
    private val timeView: TextView = view.reminder_item_time
    private var time: Instant? = null

    fun bind(reminder: Reminder) {
        titleView.text = reminder.title
        timeView.text = secondsToNow(reminder.time)
        time = reminder.time
    }

    private fun secondsToNow(time: Instant): String {
        return "${Duration.between(Instant.now(), time).seconds}s"
    }

    private fun secondsToNow(time: Instant, now: Instant): String {
        return "${Duration.between(now.truncatedTo(ChronoUnit.SECONDS), time).seconds}s"
    }

    override fun onChanged(now: Instant?) {
        val time = time
        if (time != null && now != null) {
            timeView.text = secondsToNow(time, now)
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
