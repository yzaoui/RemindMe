package com.bitwiserain.remindme

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.Instant

@Parcelize
data class NewReminder(val title: String, val time: Instant) : Parcelable
