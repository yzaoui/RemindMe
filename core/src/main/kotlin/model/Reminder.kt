package com.bitwiserain.remindme.core.model

import java.time.Instant

data class Reminder(
    val id: Int,
    val title: String,
    val time: Instant,
) {
    fun isElapsed(now: Instant): Boolean = now > time
}
