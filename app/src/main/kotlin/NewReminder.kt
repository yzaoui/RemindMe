package com.bitwiserain.remindme

import org.threeten.bp.Instant

data class NewReminder(val title: String, val time: Instant)
