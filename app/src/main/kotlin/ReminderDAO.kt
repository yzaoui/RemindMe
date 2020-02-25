package com.bitwiserain.remindme

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReminderDAO {
    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder")
    fun getReminders(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :id")
    suspend fun getReminder(id: Int): Reminder

    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}
