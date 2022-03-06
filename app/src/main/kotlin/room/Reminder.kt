package com.bitwiserain.remindme.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import com.bitwiserain.remindme.core.model.Reminder as DomainReminder

@Entity(tableName = "reminder")
data class Reminder(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time") val time: Instant,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
)
