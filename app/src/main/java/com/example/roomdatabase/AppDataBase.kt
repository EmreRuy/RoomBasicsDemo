package com.example.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeatherNote::class], version = 1)
/*
@Database(entities = [WeatherNote::class], version = 1)
Tells Room which tables (Entities) belong in this database.
Here we have only one table: WeatherNote.
version = 1 is the database schema version.
If you later add/remove columns, you increase this number
*/



/*
abstract class AppDatabase : RoomDatabase()
This is the class that represents the whole database.
It must inherit from RoomDatabase (which is provided by Room).
It’s abstract because Room will generate the actual implementation for us.
 */
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao //  This function gives access to the DAO.

    companion object {
        // This will hold ONE copy of the database for the whole app.
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance (context: Context): AppDatabase = INSTANCE ?: synchronized(this){
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "northflurry_db"
            )
                .fallbackToDestructiveMigration(false)
                .build()
                .also { INSTANCE = it }
        }
    }
}

/*
Your Entities (like WeatherNote) are the tables.
Your DAOs (like NoteDao) are the doors to get in/out of those tables.
AppDatabase is the building that holds all the tables and doors together.
 */