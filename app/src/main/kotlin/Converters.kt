package com.bitwiserain.remindme

import androidx.room.TypeConverter
import org.threeten.bp.Instant

class Converters {
    @TypeConverter
    fun instantToLong(instant: Instant): Long = instant.epochSecond

    @TypeConverter
    fun instantFromLong(epochSeconds: Long): Instant = Instant.ofEpochSecond(epochSeconds)
}
