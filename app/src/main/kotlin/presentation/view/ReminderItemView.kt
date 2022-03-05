package com.bitwiserain.remindme.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bitwiserain.remindme.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReminderItemView(title: String, time: String, expanded: Boolean, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), onClick = onClick) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f, false),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                )
                Text(
                    text = time,
                    modifier = Modifier.wrapContentWidth()
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (expanded) {
                    Button(onClick = onDelete) {
                        Text(text = stringResource(R.string.reminder_delete))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReminderItemViewShortTitle() {
    var expanded by remember { mutableStateOf(false) }
    ReminderItemView(
        title = "Short title",
        time = "Some amount of time",
        expanded = expanded,
        onClick = { expanded = !expanded },
        onDelete = {}
    )
}

@Preview
@Composable
private fun ReminderItemViewCollapsedPreview() {
    var expanded by remember { mutableStateOf(false) }
    ReminderItemView(
        title = "Collapsed with a very very very long title",
        time = "Some amount of time",
        expanded = expanded,
        onClick = { expanded = !expanded },
        onDelete = {}
    )
}

@Preview
@Composable
private fun ReminderItemViewExpandedPreview() {
    var expanded by remember { mutableStateOf(true) }
    ReminderItemView(
        title = "Expanded with a very very very long title",
        time = "Some amount of time",
        expanded = expanded,
        onClick = { expanded = !expanded },
        onDelete = {}
    )
}
