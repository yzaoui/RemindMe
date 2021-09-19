package com.bitwiserain.remindme.presentation.view

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.Tick
import com.bitwiserain.remindme.databinding.ViewReminderItemBinding
import com.bitwiserain.remindme.domain.Reminder
import java.time.Instant

class ReminderItemRecyclerViewAdapter(
    private val deleteReminder: (reminder: Reminder) -> Unit,
    private val lco: LifecycleOwner,
    initialExpandedReminderId: Int? = null,
    private val onInitialReminderExpanded: (position: Int) -> Unit
) : ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
    private val onClickExpandListener: View.OnClickListener = View.OnClickListener { v ->
        handleReminderExpansion((v.tag as Reminder).id)
    }
    private var expandedReminderId: MutableLiveData<Int> = MutableLiveData()
    private val initialExpansion = initialExpandedReminderId?.let {
        object {
            var occurred = false
            val id = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReminderViewHolder(
        binding = ViewReminderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
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

    override fun onCurrentListChanged(previousList: MutableList<Reminder>, currentList: MutableList<Reminder>) {
        // Hacky way of making sure any initially expanded reminder is only expanded at the start
        if (previousList.isEmpty() && initialExpansion?.occurred == false) {
            handleReminderExpansion(initialExpansion.id)
            onInitialReminderExpanded(currentList.indexOfFirst { it.id == initialExpansion.id })
            initialExpansion.occurred = true
        }
    }

    // If this reminder is expanded, collapse it. Otherwise, expand it.
    private fun handleReminderExpansion(reminderId: Int) = expandedReminderId.postValue(if (expandedReminderId.value != reminderId) reminderId else null)
}

class ReminderViewHolder(
    private val binding: ViewReminderItemBinding,
    expandReminderListener: View.OnClickListener,
    deleteReminder: (reminder: Reminder) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var reminder: Reminder? = null
    val tickObserver = Observer<Instant> { now: Instant? ->
        if (now != null) updateView(now)
    }
    val expandedObserver = Observer<Int> { reminderId: Int? ->
        binding.reminderDelete.visibility = if (reminderId == this.reminder?.id) View.VISIBLE else View.GONE
        binding.reminderItemTitle.setSingleLine(reminderId != this.reminder?.id)
    }

    init {
        binding.reminderDelete.setOnClickListener { this@ReminderViewHolder.reminder?.let(deleteReminder) }
        binding.root.setOnClickListener(expandReminderListener)
    }

    fun bind(reminder: Reminder) {
        binding.root.tag = reminder
        binding.reminderItemTitle.text = reminder.title
        this.reminder = reminder
        updateView(Instant.now())
    }

    private fun updateView(now: Instant) {
        val reminder = this.reminder ?: return

        binding.reminderItemTime.text = if (reminder.isElapsed(now)) binding.root.resources.getText(R.string.reminder_elapsed) else instantToFriendlyString(reminder.time, now)
    }

    private fun instantToFriendlyString(time: Instant, now: Instant): String {
        return DateUtils.getRelativeTimeSpanString(time.toEpochMilli(), now.toEpochMilli(), DateUtils.MINUTE_IN_MILLIS).toString()
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
