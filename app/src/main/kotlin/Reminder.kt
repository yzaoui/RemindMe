package com.bitwiserain.remindme

import org.threeten.bp.Instant

data class Reminder(
    val id: String,
    val title: String,
    val time: Instant
) {
    // TODO: Temporary until IDs are set up properly
    companion object {
        private var nextId: Int = 0

        fun newId(): String {
            val ret = nextId.toString()

            nextId++

            return ret
        }
    }
}
