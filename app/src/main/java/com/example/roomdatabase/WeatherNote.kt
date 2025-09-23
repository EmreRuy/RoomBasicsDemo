package com.example.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_notes")
data class WeatherNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "created_at")
val createdAt: Long = System.currentTimeMillis()
)
