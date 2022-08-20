package com.karanjotsingh.basicnotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {

    abstract val notesDao: NotesDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "notes_db"
            ).build().also { instance = it }
        }
    }
}
