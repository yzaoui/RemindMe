package com.bitwiserain.remindme.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(tableName = "reminder")
data class Reminder(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time") val time: Instant
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0

    fun isElapsed(now: Instant): Boolean = now > time
}
