package com.bitwiserain.remindme.presentation.view

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.Tick
import com.bitwiserain.remindme.core.model.Reminder
import java.time.Instant

class ReminderItemRecyclerViewAdapter(
    private val deleteReminder: (reminder: Reminder) -> Unit,
    initialExpandedReminderId: Int? = null,
    private val onInitialReminderExpanded: (position: Int) -> Unit
) : ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
    private val expandedReminderId: MutableLiveData<Int?> = MutableLiveData()
    private val initialExpansion = initialExpandedReminderId?.let {
        object {
            var occurred = false
            val id = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReminderViewHolder(
        expandedReminderId = expandedReminderId,
        timer = Tick.timer,
        composeView = ComposeView(parent.context),
        onExpandReminder = { id -> handleReminderExpansion(id) },
        onDeleteReminder = deleteReminder
    )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(previousList: MutableList<Reminder>, currentList: MutableList<Reminder>) {
        // Hacky way of making sure any initially expanded reminder is only expanded at the start
        if (previousList.isEmpty() && initialExpansion?.occurred == false) {
            handleReminderExpansion(initialExpansion.id)
            onInitialReminderExpanded(currentList.indexOfFirst { it.id == initialExpansion.id })
            initialExpansion.occurred = true
        }
    }

    override fun onViewRecycled(holder: ReminderViewHolder) {
        // Dispose of the underlying Composition of the ComposeView when RecyclerView has recycled this ViewHolder
        holder.composeView.disposeComposition()
    }

    // If this reminder is expanded, collapse it. Otherwise, expand it.
    private fun handleReminderExpansion(reminderId: Int) = expandedReminderId.postValue(if (expandedReminderId.value != reminderId) reminderId else null)
}

class ReminderViewHolder(
    private val expandedReminderId: LiveData<Int?>,
    private val timer: LiveData<Instant>,
    val composeView: ComposeView,
    private val onExpandReminder: (Int) -> Unit,
    private val onDeleteReminder: (reminder: Reminder) -> Unit
) : RecyclerView.ViewHolder(composeView) {

    init {
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    }

    fun bind(reminder: Reminder) {
        composeView.setContent {
            val expandedId = expandedReminderId.observeAsState()
            val timer = timer.observeAsState(Instant.now())
            val now = timer.value
            ReminderItemView(
                title = reminder.title,
                time = if (reminder.isElapsed(now)) itemView.resources.getString(R.string.reminder_elapsed) else instantToFriendlyString(reminder.time, now),
                expanded = expandedId.value == reminder.id,
                onClick = { onExpandReminder(reminder.id) },
                onDelete = { onDeleteReminder(reminder) }
            )
        }
    }

    private fun instantToFriendlyString(time: Instant, now: Instant): String {
        return DateUtils.getRelativeTimeSpanString(time.toEpochMilli(), now.toEpochMilli(), 0).toString()
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
