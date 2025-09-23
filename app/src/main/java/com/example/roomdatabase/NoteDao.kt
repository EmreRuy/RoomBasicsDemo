package com.example.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// we use this to talk to WeatherNote table
// NoteDao -> This is how I talk to my room table
@Dao // marks this interface as the data-access object.
interface NoteDao {
    // Using Flow so UI can observe changes automatically.
    @Query("SELECT * FROM weather_notes ORDER BY created_at DESC")
    fun getAllNotes(): Flow<List<WeatherNote>>

    @Insert
    suspend fun insert(note: WeatherNote): Long

    @Update
    suspend fun update(note: WeatherNote)

    @Delete
    suspend fun delete(note: WeatherNote)
}