package com.bitwiserain.remindme.room

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun instantToLong(instant: Instant): Long = instant.epochSecond

    @TypeConverter
    fun instantFromLong(epochSeconds: Long): Instant = Instant.ofEpochSecond(epochSeconds)
}
