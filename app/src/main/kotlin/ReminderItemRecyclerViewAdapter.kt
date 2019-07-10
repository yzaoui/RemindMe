package com.bitwiserain.remindme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitwiserain.remindme.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_reminder_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [ReminderListFragment.OnReminderItemInteractionListener].
 */
class ReminderItemRecyclerViewAdapter(private val values: List<DummyItem>, private val listener: ReminderListFragment.OnReminderItemInteractionListener?) : RecyclerView.Adapter<ReminderItemRecyclerViewAdapter.ViewHolder>() {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener?.onReminderItemInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reminder_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.timeView.text = item.time

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.reminder_item_title
        val timeView: TextView = view.reminder_item_time

        override fun toString(): String {
            return super.toString() + " '" + timeView.text + "'"
        }
    }
}
