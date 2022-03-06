package com.bitwiserain.remindme.presentation.view

import android.text.format.DateUtils
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bitwiserain.remindme.Tick
import com.bitwiserain.remindme.core.model.Reminder
import java.time.Instant

@Composable
fun ReminderListView(
    reminders: List<Reminder>,
    initiallyExpandedReminderId: Int?,
    elapsedString: String,
    onDeleteReminder: (reminder: Reminder) -> Unit
) {
    var expandedReminderId: Int? by remember { mutableStateOf(initiallyExpandedReminderId) }
    val timer = Tick.timer.observeAsState(Instant.now())
    val now = timer.value

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState
    ) {
        items(reminders) { reminder ->
            ReminderItemView(
                title = reminder.title,
                time = if (reminder.isElapsed(now)) elapsedString else instantToFriendlyString(reminder.time, now),
                expanded = expandedReminderId == reminder.id,
                onClick = { expandedReminderId = reminder.id },
                onDelete = { onDeleteReminder(reminder) }
            )
        }
    }

    if (reminders.isNotEmpty() && initiallyExpandedReminderId != null) {
        LaunchedEffect(null) {
            val reminderIndex = reminders.indexOfFirst { it.id == initiallyExpandedReminderId }

            if (reminderIndex != -1) listState.animateScrollToItem(reminderIndex)
        }
    }
}

private fun instantToFriendlyString(time: Instant, now: Instant): String {
    return DateUtils.getRelativeTimeSpanString(time.toEpochMilli(), now.toEpochMilli(), 0).toString()
}

@Preview
@Composable
private fun ReminderListViewPreview() {
    var reminders by remember {
        mutableStateOf(
            listOf(
                Reminder(id = 1, title = "Ask X about a possible extension", time = Instant.ofEpochSecond(123456L)),
                Reminder(id = 2, title = "Respond to that important email", time = Instant.ofEpochSecond(1647442604L)),
                Reminder(id = 3, title = "Transfer tuition fee", time = Instant.ofEpochSecond(1656552531L)),
                Reminder(id = 4, title = "A long and detailed description of some specific thing I must be reminded of", time = Instant.ofEpochSecond(1656552531L)),
                Reminder(id = 5, title = "A long and expanded description of some specific thing I must be reminded of", time = Instant.ofEpochSecond(1656552531L))
            )
        )
    }

    ReminderListView(
        reminders = reminders,
        initiallyExpandedReminderId = 5,
        elapsedString = "ELAPSED",
        onDeleteReminder = { reminder -> reminders = reminders.minus(reminder) }
    )
}