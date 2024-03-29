package com.bitwiserain.remindme.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDAO {
    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder")
    fun getReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :id")
    suspend fun getReminder(id: Int): Reminder

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}
