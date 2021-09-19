package com.bitwiserain.remindme.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import com.bitwiserain.remindme.domain.Reminder as DomainReminder

@Entity(tableName = "reminder")
data class Reminder(
    @ColumnInfo(name = "title") override val title: String,
    @ColumnInfo(name = "time") override val time: Instant
) : DomainReminder {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Int = 0
}
